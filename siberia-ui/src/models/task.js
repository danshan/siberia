import { paginateTaskList } from '../services/api';

export default {
  namespace: 'task',

  state: {
    taskList: {
      pagination: {},
      list: [],
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
        taskList: {
          list: action.payload.data.content,
          pagination: {
            current: action.payload.data.number,
            pageSize: action.payload.data.size,
            total: action.payload.data.totalElements,
          },
        },
      };
    },
  },
};
