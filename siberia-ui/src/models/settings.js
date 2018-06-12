import { routerRedux } from 'dva/router';
import { message } from 'antd';
import { fakeSubmitForm, findEnvList, createEnv } from '../services/api';

export default {
  namespace: 'settings',

  state: {
    envList: [],
    step: {
      payAccount: 'ant-design@alipay.com',
      receiverAccount: 'test@example.com',
      receiverName: 'Alex',
      amount: '500',
    },
  },

  effects: {
    *findEnvList({ payload }, { call, put }) {
      const response = yield call(findEnvList, payload);
      yield put({
        type: 'envList',
        payload: response,
      });
    },

    *createEnv({ payload }, { call }) {
      const response = yield call(createEnv, payload);
      this.state.envList.push(response.data);
      this.setState({ envList: this.state.envList });
    },

    *submitRegularForm({ payload }, { call }) {
      yield call(fakeSubmitForm, payload);
      message.success('提交成功');
    },
    *submitStepForm({ payload }, { call, put }) {
      yield call(fakeSubmitForm, payload);
      yield put({
        type: 'saveStepFormData',
        payload,
      });
      yield put(routerRedux.push('/form/step-form/result'));
    },
    *submitAdvancedForm({ payload }, { call }) {
      yield call(fakeSubmitForm, payload);
      message.success('提交成功');
    },
  },

  reducers: {
    envList(state, action) {
      return {
        ...state,
        envList: action.payload.data,
      };
    },

    saveStepFormData(state, { payload }) {
      return {
        ...state,
        step: {
          ...state.step,
          ...payload,
        },
      };
    },
  },
};
