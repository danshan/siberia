import { paginatePipelineList } from '../services/api';

export default {
  namespace: 'pipeline',

  state: {
    pipelineList: {},
    pipeline: {},
  },

  effects: {
    *paginatePipelineList({ payload }, { call, put }) {
      const response = yield call(paginatePipelineList, payload);
      yield put({
        type: 'paginate',
        payload: response,
      });
    },
  },

  reducers: {
    paginate(state, action) {
      return {
        ...state,
        pipelineList: action.payload,
      };
    },
  },
};
