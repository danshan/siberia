import {
  findAppConfigList,
  findAppHostList,
  loadApp,
  paginateAppLockList,
  updateAppConfig,
  updateAppHost,
  updateAppLockStatus,
} from '../services/api';

export default {
  namespace: 'app',

  state: {
    appLockList: {},
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

    *refreshAppLock({ payload }, { put }) {
      yield put({
        type: 'appLockEvent',
        payload,
      });
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

    *updateAppConfig({ payload }, { call }) {
      const response = yield call(updateAppConfig, payload);
      return response.data;
    },

    *updateAppHost({ payload }, { call }) {
      const response = yield call(updateAppHost, payload);
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
      return {
        ...state,
        app: action.payload.data,
      };
    },

    appConfigMap(state, action) {
      const configs = {};
      action.payload.data.forEach(config => {
        configs[String(config.env.id)] = JSON.stringify(config.content, null, 4);
      });
      return {
        ...state,
        appConfigMap: configs,
      };
    },

    appHostMap(state, action) {
      const hosts = {};
      action.payload.data.forEach(host => {
        hosts[String(host.env.id)] = JSON.stringify(host.hosts, null, 4);
      });
      return {
        ...state,
        appHostMap: hosts,
      };
    },

    appLockEvent(state, action) {
      const list = state.appLockList;
      const lock = list.content.find(l => l.id === action.payload.appLockId);
      if (lock) {
        lock.lockStatus = action.payload.status;
        lock.updateBy = action.payload.updateBy;
        return {
          ...state,
          appLockList: list,
        };
      }
    },
  },
};
