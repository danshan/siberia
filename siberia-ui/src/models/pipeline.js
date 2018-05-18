import { paginatePipelineList } from '../services/api';

export default {
  namespace: 'pipeline',

  state: {
    pipelineList: [],
  },

  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(paginatePipelineList, payload);
      yield put({
        type: 'queryList',
        payload: Array.isArray(response) ? response : [],
      });
    },
  },

  reducers: {
    queryList(state, action) {
      return {
        ...state,
        pipelineList: action.payload,
      };
    },
  },
};
