const pipelineList = {
  respCode: {
    code: '200',
    message: 'success',
  },
  data: {
    pageNum: 1,
    pageSize: 10,
    size: 10,
    startRow: 0,
    endRow: 9,
    total: 10,
    pages: 1,
    list: [
      {
        id: 892981422,
        title: 'Ant Design',
        description: '生命就像一盒巧克力，结果往往出人意料',
        createBy: '曲丽丽',
        updateBy: '林东东',
        createTime: '2018-05-17 14:18:40',
        updateTime: '2018-05-18 04:18:40',
      },
      {
        id: 298184187,
        title: 'Ant Design Pro',
        description: '希望是一个好东西，也许是最好的，好东西是不会消亡的',
        createBy: '朱偏右',
        updateBy: '曲丽丽',
        createTime: '2018-05-17 14:18:40',
        updateTime: '2018-05-18 00:18:40',
      },
      {
        id: 916415630,
        title: 'Bootstrap',
        description: '那是一种内在的东西， 他们到达不了，也无法触及的',
        createBy: '乐哥',
        updateBy: '周星星',
        createTime: '2018-05-17 18:18:40',
        updateTime: '2018-05-18 06:18:40',
      },
      {
        id: 1961482373,
        title: 'Bootstrap',
        description: '那是一种内在的东西， 他们到达不了，也无法触及的',
        createBy: '鱼酱',
        updateBy: '鱼酱',
        createTime: '2018-05-17 14:18:40',
        updateTime: '2018-05-18 00:18:40',
      },
      {
        id: 1359304527,
        title: 'Vue',
        description: '那时候我只会想自己想要什么，从不想自己拥有什么',
        createBy: '付小小',
        updateBy: '仲尼',
        createTime: '2018-05-17 18:18:40',
        updateTime: '2018-05-18 04:18:40',
      },
      {
        id: 1525405961,
        title: 'Vue',
        description: '城镇中有那么多的酒馆，她却偏偏走进了我的酒馆',
        createBy: '乐哥',
        updateBy: '吴加好',
        createTime: '2018-05-17 16:18:40',
        updateTime: '2018-05-18 00:18:40',
      },
      {
        id: 1953247996,
        title: 'Webpack',
        description: '那时候我只会想自己想要什么，从不想自己拥有什么',
        createBy: '谭小仪',
        updateBy: '仲尼',
        createTime: '2018-05-17 14:18:40',
        updateTime: '2018-05-18 00:18:40',
      },
      {
        id: 274086550,
        title: 'Webpack',
        description: '希望是一个好东西，也许是最好的，好东西是不会消亡的',
        createBy: '谭小仪',
        updateBy: '鱼酱',
        createTime: '2018-05-17 20:18:40',
        updateTime: '2018-05-18 06:18:40',
      },
      {
        id: 1259697073,
        title: 'Ant Design',
        description: '那时候我只会想自己想要什么，从不想自己拥有什么',
        createBy: '付小小',
        updateBy: '鱼酱',
        createTime: '2018-05-17 14:18:40',
        updateTime: '2018-05-18 02:18:40',
      },
      {
        id: 2051206487,
        title: 'Vue',
        description: '城镇中有那么多的酒馆，她却偏偏走进了我的酒馆',
        createBy: '曲丽丽',
        updateBy: '鱼酱',
        createTime: '2018-05-17 14:18:40',
        updateTime: '2018-05-18 04:18:40',
      },
    ],
    prePage: 0,
    nextPage: 0,
    isFirstPage: true,
    isLastPage: true,
    hasPreviousPage: false,
    hasNextPage: false,
    navigatePages: 8,
    navigatepageNums: [1],
    navigateFirstPage: 1,
    navigateLastPage: 1,
    firstPage: 1,
    lastPage: 1,
  },
};

const pipeline = {
  id: 2051206487,
  title: 'Vue',
  description: '城镇中有那么多的酒馆，她却偏偏走进了我的酒馆',
  createBy: '曲丽丽',
  updateBy: '鱼酱',
  createTime: '2018-05-17 14:18:40',
  updateTime: '2018-05-18 04:18:40',
};

export function paginatePipelineList(req, res) {
  const result = pipelineList;

  if (res && res.json) {
    res.json(result);
  } else {
    return result;
  }
}

export function loadPipeline(req, res) {
  const result = pipeline;

  if (res && res.json) {
    res.json(result);
  } else {
    return result;
  }
}

export default {
  paginatePipelineList,
  loadPipeline,
};
