import {
  findEnvList,
  createEnv,
  paginateAppList,
  createApp,
  updateApp,
  removeApp,
} from '../services/api';

export default {
  namespace: 'settings',

  state: {
    envList: [],
    appList: {},
    step: {
      payAccount: 'ant-design@alipay.com',
      receiverAccount: 'test@example.com',
      receiverName: 'Alex',
      amount: '500',
    },
  },

  effects: {
    *findEnvList({ payload }, { call, put }) {
      const response = yield call(findEnvList, payload);
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
      return response;
    },

    *paginateAppList({ payload }, { call, put }) {
      const response = yield call(paginateAppList, payload);
      yield put({
        type: 'appList',
        payload: response,
      });
    },

    *createApp({ payload }, { call }) {
      const response = yield call(createApp, payload);
      return response;
    },

    *updateApp({ payload }, { call }) {
      const response = yield call(updateApp, payload);
      return response;
    },

    *removeApp({ payload }, { call }) {
      const response = yield call(removeApp, payload);
      return response;
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
