const appLockList = {
  respCode: {
    code: '200',
    message: 'success',
  },
  data: {
    content: [
      {
        id: 1,
        project: 'togather',
        module: 'togather-edge',
        env: {
          id: 1,
          name: 'dev',
          description: 'develop',
          createBy: 'sys',
          updateBy: 'sys',
          createTime: '2018-06-12 07:12:06',
          updateTime: '2018-06-12 07:12:06',
        },
        lockStatus: {
          value: 1,
          desc: 'locked',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-06-12 07:12:06',
        updateTime: '2018-06-12 07:12:06',
        app: 'togather.togather-edge',
      },
      {
        id: 2,
        project: 'togather',
        module: 'togather-edge',
        env: {
          id: 2,
          name: 'sit',
          description: 'sit',
          createBy: 'sys',
          updateBy: 'sys',
          createTime: '2018-06-12 07:12:06',
          updateTime: '2018-06-12 07:12:06',
        },
        lockStatus: {
          value: 0,
          desc: 'unlocked',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-06-12 07:12:06',
        updateTime: '2018-06-12 07:12:06',
        app: 'togather.togather-edge',
      },
      {
        id: 3,
        project: 'togather',
        module: 'togather-edge',
        env: {
          id: 3,
          name: 'prod',
          description: 'production',
          createBy: 'sys',
          updateBy: 'sys',
          createTime: '2018-06-12 07:12:06',
          updateTime: '2018-06-12 07:12:06',
        },
        lockStatus: {
          value: 1,
          desc: 'locked',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-06-12 07:12:06',
        updateTime: '2018-06-12 07:12:06',
        app: 'togather.togather-edge',
      },
      {
        id: 4,
        project: 'togather',
        module: 'togather-news',
        env: {
          id: 1,
          name: 'dev',
          description: 'develop',
          createBy: 'sys',
          updateBy: 'sys',
          createTime: '2018-06-12 07:12:06',
          updateTime: '2018-06-12 07:12:06',
        },
        lockStatus: {
          value: 1,
          desc: 'locked',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-06-12 07:12:06',
        updateTime: '2018-06-12 07:12:06',
        app: 'togather.togather-news',
      },
      {
        id: 5,
        project: 'togather',
        module: 'togather-todo',
        env: {
          id: 2,
          name: 'sit',
          description: 'sit',
          createBy: 'sys',
          updateBy: 'sys',
          createTime: '2018-06-12 07:12:06',
          updateTime: '2018-06-12 07:12:06',
        },
        lockStatus: {
          value: 0,
          desc: 'unlocked',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-06-12 07:12:06',
        updateTime: '2018-06-12 07:12:06',
        app: 'togather.togather-todo',
      },
      {
        id: 6,
        project: 'togather',
        module: 'togather-task',
        env: {
          id: 3,
          name: 'prod',
          description: 'production',
          createBy: 'sys',
          updateBy: 'sys',
          createTime: '2018-06-12 07:12:06',
          updateTime: '2018-06-12 07:12:06',
        },
        lockStatus: {
          value: 1,
          desc: 'locked',
        },
        createBy: 'sys',
        updateBy: 'sys',
        createTime: '2018-06-12 07:12:06',
        updateTime: '2018-06-12 07:12:06',
        app: 'togather.togather-task',
      },
    ],
    last: true,
    totalPages: 1,
    totalElements: 6,
    size: 10,
    number: 0,
    first: true,
    sort: null,
    numberOfElements: 6,
  },
};

export function paginateAppLockList(req, res) {
  const result = appLockList;

  if (res && res.json) {
    res.json(result);
  } else {
    return result;
  }
}

export default {
  paginateAppLockList,
};
