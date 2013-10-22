var data;
var villages;
var spawn;
var popuping = false;
var item_num = 0;
var item_array = new Array();

function format(f) {
  return a = arguments, f.replace(/\{(\d)\}/g, function(m, c) {
    return a[parseInt(c) + 1];
  });
}

function getIconPath(name, isBlock) {
  name = name.toLowerCase().replace("'", '').replace(' ', '_').replace(' ', '_');
  return format('style/{0}/{1}.png', (isBlock) ? 'blocks' : 'items', name);
}

function createItem(item) {
  if (!item)
  {
    return '<div class="item"></div>';
  }
  else {
    item_array[item_num] = item;
    return format('<div id="item_{0}" class="item{1}" style="background-image: url({2})">{3}{4}</div>',
            item_num++,
            (item.e) ? ' item-enchant' : '',
            getIconPath(item.n, (item.i < 256)),
            (item.c === 1) ? '' : item.c,
            (item.e) ? 'E' : '');
  }
}

function getVillageStats(i) {
  var villager = Enumerable.From(villages[i].r);
  var count = function(cond) {
    return villager.Count('r => ' + cond);
  };
  return format('<b>人口: {0}</b> (大人: {1}, 子供: {2})<br /><b>取引可能: {3}</b><br />\n\
農民: {4}<br />司書: {5}<br />司祭: {6}<br />鍛冶屋: {7}<br />肉屋: {8}',
          villager.Count(), count('r.a'), count('!r.a'), count('r.a && r.r'),
          count('r.a && r.p === 0'), count('r.a && r.p === 1'), count('r.a && r.p === 2'),
          count('r.a && r.p === 3'), count('r.a && r.p === 4'));
}

function getItemDescript(item) {
  return format('<b><i>{0}</i></b> {1}<br />', item.n, (item.c === 1) ? '' : 'x' + item.c) +
          ((item.e) ? Enumerable.From(item.e).Select('e=>format("{0} Lv.{1}",enchants[e.i],e.l)').ToArray().join('<br />') : '');
}

function getDistance(a, b) {
  return Math.sqrt(Math.pow(a[0] - b[0], 2.0) + Math.pow(a[1] - b[1], 2.0) + Math.pow(a[2] - b[2], 2.0));
}

function getPopgraph(village) {
  var popular = Enumerable.From(village.r).Count();
  var adult = Enumerable.From(village.r).Count('r => r.a');
  var adult_p = (adult / popular) * 100;
  return format(['<tr><td colspan="7"><span class="popgraph_text">{0}</span><div class="popgraph">',
    '<div class="popgraph_adult" style="width:{1}%"></div>',
    '<div class="popgraph_child" style="width:{2}%"></div>',
    '</div></td></tr>'].join(''), popular, adult_p, 100 - adult_p);
}

function getCoordinate(village) {
  var x = village.c[0], y = village.c[1], z = village.c[2];
  return format('{3}({0}, {1}, {2}){4}', x, y, z,
    (dynmap_addr) ? format('<a href="{0}&x={1}&y={2}&z={3}" target="_blank">', dynmap_addr, x, y, z) : '',
    (dynmap_addr) ? '</a>' : '');
}

function create() {
  $('.content').children().remove();
  spawn = data.s;
  villages = Enumerable.From(data.v).OrderBy('v => getDistance(v.c, spawn)').ToArray();
  for (var i = 0; i < villages.length; i++) {
    var v = villages[i];
    {
      var pos = getCoordinate(v);
      var distance = Math.round(getDistance(v.c, spawn) * 10) / 10;
      $('.content').append([format('<table id="village_{0}">', i),
        '<tr>',
        format('<th>村 {0}</th>', i + 1),
        format('<td>座標: {0}</td>', pos),
        format('<td>距離: {0}m</td>', distance),
        format('<td>半径: {0}m</td>', v.s),
        format('<td>家屋: {0}軒</td>', v.d),
        format('<td>推定人口: {0}</td>', v.p),
        format('<td>繁殖: {0}</td>', (v.d * 0.35 > Enumerable.From(v.r).Count('r => r.a')) ? '可' : '不可'),
        '</tr>',
        getPopgraph(v), '</table>'].join(''));
      setPopup($(format('#village_{0} tr div.popgraph', i)), getVillageStats(i));
    }

    var villager = Enumerable.From(v.r).Where('v => v.a && v.r != null').ToArray();
    var list = '<table class="connect">';
    for (var j = 0; j < villager.length; j++) {
      var r = villager[j];
      list += '<tr>';
      list += format('<td class="profession profession_{0}"></td>', r.p);
      for (var k = 0; k < r.r.length; k++) {
        var t = r.r[k];
        list += format('<td{0}>{1}{2}<div class="arrow">{4} / {5}</div>{3}</td>',
                (t.u === t.m) ? ' class="uses_max"' : '',
                createItem(t.a),
                createItem(t.b),
                createItem(t.s),
                t.u,
                t.m);
      }

      list += '</tr>';
    }

    $('.content').append(list + '</table>');
  }

  setPopup($('.profession_0'), '職業: 農民');
  setPopup($('.profession_1'), '職業: 司書');
  setPopup($('.profession_2'), '職業: 司祭');
  setPopup($('.profession_3'), '職業: 鍛冶屋');
  setPopup($('.profession_4'), '職業: 肉屋');
  for (var i = 0; i < item_num; i++) {
    setPopup($('#item_' + i), getItemDescript(item_array[i]));
  }
}

function setPopup(target, text) {
  target.hover(function() {
    $('.popup').stop(true, true).show().html(text);
    popuping = true;
  }, function() {
    $('.popup').stop(true, true).fadeOut(100);
    popuping = false;
  });
}

function update() {
  $('#updateTime').text("更新中...");
  $.getJSON(output_json, function(source) {
    data = source;
    item_num = 0;
    item_array = new Array();
    $('#updateTime').text(new Date(source.t).toLocaleTimeString());
    create();
    
    setTimeout(update, 1000 * 60 * 5);
  });
}

$(function() {
  $('*').mousemove(function(e) {
    if (popuping)
      $('.popup').css({left: e.pageX + 16 + 'px', top: e.pageY + 16 + 'px'});
  });
  
  update();
});