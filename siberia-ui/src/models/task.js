import { paginateTaskList, createTask } from '../services/api';

export default {
  namespace: 'task',

  state: {
    taskList: {},
  },

  effects: {
    *paginateTaskList({ payload }, { call, put }) {
      const response = yield call(paginateTaskList, payload);
      yield put({
        type: 'taskList',
        payload: response,
      });
    },

    *createTask({ payload }, { call, put }) {
      const response = yield call(createTask, payload);
      yield put({
        type: 'createdTask',
        payload: response,
      });
    },
  },

  reducers: {
    taskList(state, action) {
      return {
        ...state,
        taskList: action.payload.data,
      };
    },
  },
};
