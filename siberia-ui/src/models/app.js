import {
  findAppHostList,
  loadApp,
  updateAppConfig,
  paginateAppLockList,
  updateAppLockStatus,
} from '../services/api';

export default {
  namespace: 'app',

  state: {
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

    *findAppHostList({ payload }, { call, put }) {
      const response = yield call(findAppHostList, payload);
      yield put({
        type: 'appHostMap',
        payload: response,
      });
    },

    *updateAppConfig({ payload }, { call }) {
      const response = yield call(updateAppConfig, payload);
      return response.data;
    },
  },

  reducers: {
    appLockList(state, action) {
      return {
        ...state,
        appLockList: action.payload.data,
      };
    },

    app(state, action) {
      const configs = {};
      action.payload.data.configs.forEach(config => {
        configs[String(config.env.id)] = JSON.stringify(config.content, null, 4);
      });

      return {
        ...state,
        app: action.payload.data,
        appConfigMap: configs,
      };
    },
  },
};
