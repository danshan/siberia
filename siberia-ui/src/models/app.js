import {
  paginateAppLockList,
  updateAppLockStatus,
  loadApp,
  findAppConfigList,
  findAppHostList,
} from '../services/api';

export default {
  namespace: 'app',

  state: {
    envList: [],

    appLockList: {
      pagination: {},
      list: [],
    },

    app: {},
    appConfigMap: {},
    appHostMap: {},
  },

  effects: {
    *paginateAppLockList({ payload }, { call, put }) {
      const response = yield call(paginateAppLockList, payload);
      yield put({
        type: 'appLockList',
        payload: response,
      });
    },

    *updateAppLockStatus({ payload }, { call }) {
      const response = yield call(updateAppLockStatus, payload);
      return response.data;
    },

    *loadApp({ payload }, { call, put }) {
      const response = yield call(loadApp, payload);
      yield put({
        type: 'app',
        payload: response,
      });
    },

    *findAppConfigList({ payload }, { call, put }) {
      const response = yield call(findAppConfigList, payload);
      yield put({
        type: 'appConfigMap',
        payload: response,
      });
    },

    *findAppHostList({ payload }, { call, put }) {
      const response = yield call(findAppHostList, payload);
      yield put({
        type: 'appHostMap',
        payload: response,
      });
    },
  },

  reducers: {
    appLockList(state, action) {
      return {
        ...state,
        appLockList: {
          list: action.payload.data.content,
          pagination: {
            current: action.payload.data.number,
            pageSize: action.payload.data.size,
            total: action.payload.data.totalElements,
          },
        },
      };
    },

    app(state, action) {
      return {
        ...state,
        app: action.payload.data,
      };
    },

    appConfigMap(state, action) {
      const appConfigMap = action.payload.data.reduce((map, obj) => {
        map[obj.env.id + ''] = obj; // eslint-disable-line
        return map;
      }, {});
      return {
        ...state,
        appConfigMap,
      };
    },
  },
};
