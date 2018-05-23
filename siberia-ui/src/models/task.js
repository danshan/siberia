import { paginateTaskList } from '../services/api';

export default {
  namespace: 'task',

  state: {
    taskList: {
      data: {
        total: 0,
        size: 0,
        list: [],
      },
    },
  },

  effects: {
    *paginateTaskList({ payload }, { call, put }) {
      const response = yield call(paginateTaskList, payload);
      yield put({
        type: 'taskList',
        payload: response,
      });
    },
  },

  reducers: {
    taskList(state, action) {
      return {
        ...state,
        taskList: action.payload,
      };
    },
  },
};
