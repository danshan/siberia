import { paginateAppLockList } from '../services/api';

export default {
  namespace: 'app',

  state: {
    appLockList: {
      pagination: {},
      list: [],
    },
  },

  effects: {
    *paginateAppLockList({ payload }, { call, put }) {
      const response = yield call(paginateAppLockList, payload);
      yield put({
        type: 'appLockList',
        payload: response,
      });
    },
  },

  reducers: {
    appLockList(state, action) {
      return {
        ...state,
        appLockList: {
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
