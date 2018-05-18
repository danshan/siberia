import { parse } from 'url';

const desc = [
  '那是一种内在的东西， 他们到达不了，也无法触及的',
  '希望是一个好东西，也许是最好的，好东西是不会消亡的',
  '生命就像一盒巧克力，结果往往出人意料',
  '城镇中有那么多的酒馆，她却偏偏走进了我的酒馆',
  '那时候我只会想自己想要什么，从不想自己拥有什么',
];

const title = [
  'Alipay',
  'Angular',
  'Ant Design',
  'Ant Design Pro',
  'Bootstrap',
  'React',
  'Vue',
  'Webpack',
];

const user = [
  '付小小',
  '曲丽丽',
  '林东东',
  '周星星',
  '吴加好',
  '朱偏右',
  '鱼酱',
  '乐哥',
  '谭小仪',
  '仲尼',
];

export function fakePipeline(count) {
  const list = [];
  for (let i = 0; i < count; i += 1) {
    list.push({
      id: i,
      title: title[i % title.length],
      desc: desc[i % desc.length],
      createBy: user[i % user.length],
      updateBy: user[i % user.length],
      createTime: new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * i),
      updateTime: new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * i),
    });
  }
  return list;
}

export function paginatePipelineList(req, res, u) {
  let url = u;
  if (!url || Object.prototype.toString.call(url) !== '[object String]') {
    url = req.url; // eslint-disable-line
  }

  const params = parse(url, true).query;

  const count = params.count * 1 || 20;

  const result = fakePipeline(count);

  if (res && res.json) {
    res.json(result);
  } else {
    return result;
  }
}

export default {
  paginatePipelineList,
};
