import { routerRedux } from 'dva/router';
import {
  createPipeline,
  loadPipeline,
  paginatePipelineDeploymentList,
  paginatePipelineList,
  createPipelineDeployment,
  createPipelineTask,
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
      pagination: {},
      list: [],
    },
    createdPipelineDeployment: {},
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

    *createPipelineTask({ payload }, { call, put }) {
      const response = yield call(createPipelineTask, payload);
      yield put({
        type: 'createdPipelineTask',
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
        pipelineDeploymentList: {
          list: action.payload.data.content,
          pagination: {
            current: action.payload.data.number,
            pageSize: action.payload.data.size,
            total: action.payload.data.totalElements,
          },
        },
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
