import {
  paginatePipelineList,
  loadPipeline,
  paginatePipelineDeploymentList,
} from '../services/api';

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
    pipelineDeploymentList: {
      data: {
        total: 0,
        size: 0,
        list: [],
      },
    },
  },

  effects: {
    *paginatePipelineList({ payload }, { call, put }) {
      const response = yield call(paginatePipelineList, payload);
      yield put({
        type: 'pipelineList',
        payload: response,
      });
    },

    *loadPipeline({ payload }, { call, put }) {
      const response = yield call(loadPipeline, payload);
      yield put({
        type: 'pipeline',
        payload: response,
      });
    },

    *paginatePipelineDeploymentList({ payload }, { call, put }) {
      const response = yield call(paginatePipelineDeploymentList, payload);
      yield put({
        type: 'pipelineDeploymentList',
        payload: response,
      });
    },
  },

  reducers: {
    pipelineList(state, action) {
      return {
        ...state,
        pipelineList: action.payload,
      };
    },

    pipeline(state, action) {
      return {
        ...state,
        pipeline: action.payload,
      };
    },

    pipelineDeploymentList(state, action) {
      return {
        ...state,
        pipelineDeploymentList: action.payload,
      };
    },
  },
};
