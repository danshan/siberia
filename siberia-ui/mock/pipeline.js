const pipelineList = {
  respCode: {
    code: '200',
    message: 'success',
  },
  data: {
    content: [
      {
        id: 1,
        title: 'demo',
        description: 'pipeline for demo',
        status: {
          value: 2,
          desc: 'archived',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-07-05 02:04:56',
        updateTime: '2018-07-05 02:54:13',
      },
      {
        id: 2,
        title: 'demo',
        description: 'pipeline for demo',
        status: {
          value: 1,
          desc: 'running',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-07-05 02:04:56',
        updateTime: '2018-07-05 02:04:56',
      },
    ],
    last: true,
    totalPages: 1,
    totalElements: 2,
    size: 10,
    number: 0,
    first: true,
    sort: null,
    numberOfElements: 2,
  },
};

const pipeline = {
  respCode: {
    code: '200',
    message: 'success',
  },
  data: {
    id: 2051206487,
    title: 'Vue',
    description: '城镇中有那么多的酒馆，她却偏偏走进了我的酒馆',
    createBy: '曲丽丽',
    updateBy: '鱼酱',
    createTime: '2018-05-17 14:18:40',
    updateTime: '2018-05-18 04:18:40',
  },
};

const pipelineDeploymentList = {
  respCode: {
    code: '200',
    message: 'success',
  },
  data: {
    content: [
      {
        id: 12449006,
        pipelineId: 1207371172,
        project: 'apple',
        module: 'apple-ui',
        buildNo: 0,
        taskList: null,
        createBy: '仲尼',
        updateBy: '吴加好',
        createTime: '2018-05-21 14:54:05',
        updateTime: '2018-05-22 00:54:05',
      },
      {
        id: 23403620,
        pipelineId: 1561614646,
        project: 'amazon',
        module: 'amazon-ui',
        buildNo: 0,
        taskList: null,
        createBy: '朱偏右',
        updateBy: '乐哥',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-21 20:54:05',
      },
      {
        id: 1710287873,
        pipelineId: 1604255916,
        project: 'google',
        module: 'google-ui',
        buildNo: 0,
        taskList: null,
        createBy: '吴加好',
        updateBy: '乐哥',
        createTime: '2018-05-21 12:54:05',
        updateTime: '2018-05-21 22:54:05',
      },
      {
        id: 146669268,
        pipelineId: 272465141,
        project: 'google',
        module: 'google-ui',
        buildNo: 0,
        taskList: null,
        createBy: '朱偏右',
        updateBy: '仲尼',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-21 22:54:05',
      },
      {
        id: 433897083,
        pipelineId: 1129606955,
        project: 'amazon',
        module: 'amazon-service',
        buildNo: 0,
        taskList: null,
        createBy: '谭小仪',
        updateBy: '吴加好',
        createTime: '2018-05-21 16:54:05',
        updateTime: '2018-05-21 22:54:05',
      },
      {
        id: 200401768,
        pipelineId: 718713811,
        project: 'google',
        module: 'google-ui',
        buildNo: 0,
        taskList: null,
        createBy: '朱偏右',
        updateBy: '付小小',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-22 02:54:05',
      },
      {
        id: 1443806493,
        pipelineId: 1262855705,
        project: 'amazon',
        module: 'amazon-service',
        buildNo: 0,
        taskList: null,
        createBy: '谭小仪',
        updateBy: '仲尼',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-22 02:54:05',
      },
      {
        id: 1991393904,
        pipelineId: 851348246,
        project: 'amazon',
        module: 'amazon-gateway',
        buildNo: 0,
        taskList: null,
        createBy: '周星星',
        updateBy: '朱偏右',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-22 02:54:05',
      },
      {
        id: 423881159,
        pipelineId: 663889163,
        project: 'apple',
        module: 'apple-gateway',
        buildNo: 0,
        taskList: null,
        createBy: '仲尼',
        updateBy: '林东东',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-21 20:54:05',
      },
      {
        id: 947472733,
        pipelineId: 1451454117,
        project: 'amazon',
        module: 'amazon-service',
        buildNo: 0,
        taskList: null,
        createBy: '周星星',
        updateBy: '乐哥',
        createTime: '2018-05-21 10:54:05',
        updateTime: '2018-05-21 22:54:05',
      },
    ],
    last: true,
    totalPages: 10,
    totalElements: 100,
    size: 10,
    number: 0,
    sort: null,
    first: true,
    numberOfElements: 10,
  },
};

const createdDeployment = payload => {
  return {
    respCode: {
      code: '200',
      message: 'success',
    },
    data: payload,
  };
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

export function createPipeline(req, res) {
  const result = pipeline;

  if (res && res.json) {
    res.json(result);
  } else {
    return result;
  }
}

export function paginatePipelineDeploymentList(req, res) {
  const result = pipelineDeploymentList;

  if (res && res.json) {
    res.json(result);
  } else {
    return result;
  }
}

export function createPipelineDeployment(req, res) {
  res.json(createdDeployment(req.body));
}

export default {
  paginatePipelineList,
  loadPipeline,
  createPipeline,
  paginatePipelineDeploymentList,
  createPipelineDeployment,
};
