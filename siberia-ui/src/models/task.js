import { createTask, paginateTaskList, rollbackTask, redeployTask } from '../services/api';

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

    *refreshTask({ payload }, { put }) {
      yield put({
        type: 'taskEvent',
        payload,
      });
    },

    *rollbackTask({ payload }, { call }) {
      const response = yield call(rollbackTask, payload);
      return response;
    },

    *redeployTask({ payload }, { call }) {
      const response = yield call(redeployTask, payload);
      return response;
    },
  },

  reducers: {
    taskList(state, action) {
      return {
        ...state,
        taskList: action.payload.data,
      };
    },

    taskEvent(state, action) {
      const list = state.taskList;
      const task = list.content.find(l => l.id === action.payload.taskId);
      if (task) {
        task.updateTime = action.payload.updateTime;
        task.status = action.payload.status;
        return {
          ...state,
          taskList: list,
        };
      }
    },
  },
};
