import { paginateEnvList, createEnv, paginateAppList } from '../services/api';

export default {
  namespace: 'settings',

  state: {
    envList: {},
    appList: {},
    step: {
      payAccount: 'ant-design@alipay.com',
      receiverAccount: 'test@example.com',
      receiverName: 'Alex',
      amount: '500',
    },
  },

  effects: {
    *paginateEnvList({ payload }, { call, put }) {
      const response = yield call(paginateEnvList, payload);
      yield put({
        type: 'envList',
        payload: response,
      });
    },

    *createEnv({ payload }, { call, put }) {
      const response = yield call(createEnv, payload);
      yield put({
        type: 'createdEnv',
        payload: response,
      });
    },

    *paginateAppList({ payload }, { call, put }) {
      const response = yield call(paginateAppList, payload);
      yield put({
        type: 'appList',
        payload: response,
      });
    },
  },

  reducers: {
    envList(state, action) {
      return {
        ...state,
        envList: action.payload.data,
      };
    },

    createdEnv(state, action) {
      return {
        ...state,
        env: action.payload.data,
      };
    },

    appList(state, action) {
      return {
        ...state,
        appList: action.payload.data,
      };
    },
  },
};
