import { findSiberiaLogs } from '../services/api';

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
  },

  reducers: {
    siberiaLogs(state, action) {
      return {
        ...state,
        siberiaLogs: action.payload.data,
      };
    },
  },
};
