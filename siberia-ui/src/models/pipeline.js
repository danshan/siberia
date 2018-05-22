import { paginatePipelineList, loadPipeline } from '../services/api';

export default {
  namespace: 'pipeline',

  state: {
    pipelineList: {
      data: {
        total: 0,
        size: 0,
        list: [],
      },
    },
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

    *loadPipeline({ payload }, { call, put }) {
      const response = yield call(loadPipeline, payload);
      yield put({
        type: 'load',
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

    load(state, action) {
      return {
        ...state,
        pipeline: action.payload,
      };
    },
  },
};
