import { paginateAppLockList, paginateEnvList, updateAppLockStatus } from '../services/api';

export default {
  namespace: 'app',

  state: {
    envList: [],

    appLockList: {
      pagination: {},
      list: [],
    },
  },

  effects: {
    *paginateAppLockList({ payload }, { call, put }) {
      const response = yield call(paginateAppLockList, payload);
      yield put({
        type: 'appLockList',
        payload: response,
      });
    },

    *paginateEnvList({ payload }, { call, put }) {
      const response = yield call(paginateEnvList, payload);
      yield put({
        type: 'envList',
        payload: response,
      });
    },

    *updateAppLockStatus({ payload }, { call }) {
      const response = yield call(updateAppLockStatus, payload);
      return response.data;
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

    envList(state, action) {
      return {
        ...state,
        envList: action.payload.data.content,
      };
    },
  },
};
