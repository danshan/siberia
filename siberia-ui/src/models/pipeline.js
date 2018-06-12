import { routerRedux } from 'dva/router';
import {
  createPipeline,
  loadPipeline,
  paginatePipelineDeploymentList,
  paginatePipelineList,
} from '../services/api';

export default {
  namespace: 'pipeline',

  state: {
    pipelineList: {
      total: 0,
      size: 0,
      list: [],
    },
    pipeline: {},
    pipelineDeploymentList: {
      total: 0,
      size: 0,
      list: [],
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

    *createPipeline({ payload }, { call, put }) {
      const response = yield call(createPipeline, payload);
      yield put({
        type: 'created',
        payload: response,
      });
      if (response.data.id) {
        yield put(routerRedux.push(`/deployment/pipelines/${response.data.id}`));
      }
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
        pipelineList: action.payload.data,
      };
    },

    pipeline(state, action) {
      return {
        ...state,
        pipeline: action.payload.data,
      };
    },

    pipelineDeploymentList(state, action) {
      return {
        ...state,
        pipelineDeploymentList: action.payload.data,
      };
    },

    created(state, action) {
      return {
        ...state,
        pipeline: action.payload.data,
      };
    },
  },
};
