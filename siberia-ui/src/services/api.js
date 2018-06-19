import { stringify } from 'qs';
import request from '../utils/request';

export async function queryProjectNotice() {
  return request('/api/project/notice');
}

export async function queryActivities() {
  return request('/api/activities');
}

export async function queryRule(params) {
  return request(`/api/rule?${stringify(params)}`);
}

export async function removeRule(params) {
  return request('/api/rule', {
    method: 'POST',
    body: {
      ...params,
      method: 'delete',
    },
  });
}

export async function addRule(params) {
  return request('/api/rule', {
    method: 'POST',
    body: {
      ...params,
      method: 'post',
    },
  });
}

export async function fakeSubmitForm(params) {
  return request('/api/forms', {
    method: 'POST',
    body: params,
  });
}

export async function fakeChartData() {
  return request('/api/fake_chart_data');
}

export async function queryTags() {
  return request('/api/tags');
}

export async function queryBasicProfile() {
  return request('/api/profile/basic');
}

export async function queryAdvancedProfile() {
  return request('/api/profile/advanced');
}

export async function queryFakeList(params) {
  return request(`/api/fake_list?${stringify(params)}`);
}

export async function fakeAccountLogin(params) {
  return request('/api/login/account', {
    method: 'POST',
    body: params,
  });
}

export async function fakeRegister(params) {
  return request('/api/register', {
    method: 'POST',
    body: params,
  });
}

export async function queryNotices() {
  return request('/api/notices');
}

// pipelines

export async function paginatePipelineList() {
  return request('/api/pipelines');
}

export async function loadPipeline(params) {
  return request(`/api/pipelines/${params.pipelineId}`);
}

export async function createPipeline(params) {
  return request('/api/pipelines', {
    method: 'POST',
    body: params,
  });
}

export async function createPipelineDeployment(params) {
  return request(`/api/pipelines/${params.pipelineId}/deployments`, {
    method: 'POST',
    body: params,
  });
}

export async function createPipelineTask(params) {
  return request(`/api/pipelines/${params.pipelineId}/tasks`, {
    method: 'POST',
    body: params,
  });
}

// apps

export async function paginateAppLockList() {
  return request('/api/apps/locks');
}

export async function paginatePipelineDeploymentList(params) {
  return request(`/api/pipelines/${params.pipelineId}/deployments`);
}

// tasks

export async function paginateTaskList() {
  return request('/api/tasks');
}

// settings

export async function paginateEnvList() {
  return request('/api/settings/envs');
}

export async function createEnv(params) {
  return request('/api/settings/envs', {
    method: 'POST',
    body: params,
  });
}

export async function paginateAppList() {
  return request('/api/apps');
}
