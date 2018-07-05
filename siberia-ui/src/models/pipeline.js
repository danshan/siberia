import { routerRedux } from 'dva/router';
import {
  createPipeline,
  updatePipeline,
  loadPipeline,
  updatePipelineStatus,
  paginatePipelineDeploymentList,
  paginatePipelineList,
  createPipelineDeployment,
  findDeploymentProcessList,
} from '../services/api';

export default {
  namespace: 'pipeline',

  state: {
    pipelineList: {},
    pipeline: {},
    pipelineDeploymentList: {},
    createdPipelineDeployment: {},

    deploymentProcessMap: {},
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
        type: 'createdPipeline',
        payload: response,
      });
      if (response.data.id) {
        yield put(routerRedux.push(`/deployment/pipelines/${response.data.id}`));
      }
    },

    *updatePipeline({ payload }, { call }) {
      const response = yield call(updatePipeline, payload);
      return response.data;
    },

    *updatePipelineStatus({ payload }, { call }) {
      const response = yield call(updatePipelineStatus, payload);
      return response.data;
    },

    *paginatePipelineDeploymentList({ payload }, { call, put }) {
      const response = yield call(paginatePipelineDeploymentList, payload);
      yield put({
        type: 'pipelineDeploymentList',
        payload: response,
      });
    },

    *createPipelineDeployment({ payload }, { call, put }) {
      const response = yield call(createPipelineDeployment, payload);
      yield put({
        type: 'createdPipelineDeployment',
        payload: response,
      });
      return response.data;
    },

    *findDeploymentProcessList({ payload }, { call }) {
      const response = yield call(findDeploymentProcessList, payload);
      return response.data;
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

    createdPipeline(state, action) {
      return {
        ...state,
        pipeline: action.payload.data,
      };
    },

    createdPipelineDeployment(state, action) {
      return {
        ...state,
        createdPipelineDeployment: action.payload.data,
      };
    },
  },
};
