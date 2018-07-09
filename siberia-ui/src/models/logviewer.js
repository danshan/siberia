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

    *pushSiberiaLog({ payload }, { put }) {
      yield put({
        type: 'siberiaLine',
        payload,
      });
    },

    *pushAnsibleLog({ payload }, { put }) {
      yield put({
        type: 'ansibleLine',
        payload,
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

    siberiaLine(state, action) {
      const log = state.siberiaLogs;
      log.push(action.payload.line);
      return {
        ...state,
        siberiaLogs: log,
      };
    },

    ansibleLine(state, action) {
      const log = state.ansibleLogs;
      log.push(action.payload.line);
      return {
        ...state,
        ansibleLine: log,
      };
    },
  },
};
