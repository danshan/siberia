import { findSiberiaLogs, findAnsibleLogs } from '../services/api';

export default {
  namespace: 'logviewer',

  state: {
    siberiaLogs: [],
    ansibleLogs: [],
  },

  effects: {
    *findSiberiaLogs({ payload }, { call, put }) {
      const response = yield call(findSiberiaLogs, payload);
      yield put({
        type: 'siberiaLogs',
        payload: response,
      });
    },

    *findAnsibleLogs({ payload }, { call, put }) {
      const response = yield call(findAnsibleLogs, payload);
      yield put({
        type: 'ansibleLogs',
        payload: response,
      });
    },
  },

  reducers: {
    siberiaLogs(state, action) {
      return {
        ...state,
        siberiaLogs: action.payload.data,
      };
    },

    ansibleLogs(state, action) {
      return {
        ...state,
        ansibleLogs: action.payload.data,
      };
    },
  },
};
