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

export async function paginatePipelineList(params) {
  return request('/api/pipelines', {
    method: 'GET',
    body: params,
  });
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

export async function updatePipeline(params) {
  return request(`/api/pipelines/${params.pipelineId}`, {
    method: 'PUT',
    body: params,
  });
}

export async function updatePipelineStatus(params) {
  return request(`/api/pipelines/${params.pipelineId}/status`, {
    method: 'PUT',
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

export async function paginatePipelineDeploymentList(params) {
  return request(`/api/pipelines/${params.pipelineId}/deployments`);
}

// apps

export async function paginateAppLockList(params) {
  return request('/api/apps/locks', {
    method: 'GET',
    body: params,
  });
}

export async function updateAppLockStatus(params) {
  return request(`/api/apps/locks`, {
    method: 'PUT',
    body: params,
  });
}

export async function paginateAppList() {
  return request('/api/apps');
}

export async function removeApp(params) {
  return request(`/api/apps/${params.appId}`, {
    method: 'DELETE',
  });
}

export async function createApp(params) {
  return request(`/api/apps`, {
    method: 'POST',
    body: params,
  });
}

export async function updateApp(params) {
  return request(`/api/apps/${params.id}`, {
    method: 'PUT',
    body: params,
  });
}

export async function loadApp(params) {
  return request(`/api/apps/${params.appId}`, {
    method: 'GET',
    body: params,
  });
}

export async function findAppConfigList(params) {
  return request(`/api/apps/${params.appId}/configs`, {
    method: 'GET',
    body: params,
  });
}

export async function updateAppConfig(params) {
  return request(`/api/apps/${params.appId}/configs`, {
    method: 'PUT',
    body: params,
  });
}

export async function findAppHostList(params) {
  return request(`/api/apps/${params.appId}/hosts`, {
    method: 'GET',
    body: params,
  });
}

// tasks

export async function paginateTaskList() {
  return request('/api/tasks');
}

// settings

export async function findEnvList() {
  return request('/api/settings/envs');
}

export async function createEnv(params) {
  return request('/api/settings/envs', {
    method: 'POST',
    body: params,
  });
}

export async function updateEnv(params) {
  return request(`/api/settings/envs/${params.id}`, {
    method: 'PUT',
    body: params,
  });
}

// logs

export async function findSiberiaLogs(params) {
  return request(`/api/logs/siberialog/${params.id}`, {
    method: 'GET',
    body: params,
  });
}

export async function findAnsibleLogs(params) {
  return request(`/api/logs/ansiblelog/${params.id}`, {
    method: 'GET',
    body: params,
  });
}
