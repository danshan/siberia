import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Switch, Icon, Radio, Table } from 'antd';

import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './AppLockList.less';

@connect(({ app, settings, loading }) => ({
  app,
  settings,
  appLoading: loading.models.app,
  settingsLoading: loading.models.settings,
}))
export default class AppConfigList extends PureComponent {
  state = {
    envId: 0,
    pageNum: 0,
    pageSize: 20,
  };

  componentDidMount() {
    this.paginateAppLockList();
    this.findEnvList();
  }

  paginateAppLockList = () => {
    this.props.dispatch({
      type: 'app/paginateAppLockList',
      payload: {
        pageNum: this.state.pageNum,
        pageSize: this.state.pageSize,
        status: this.state.status,
        envId: this.state.envId,
      },
    });
  };

  findEnvList = () => {
    this.props.dispatch({
      type: 'settings/findEnvList',
      payload: {},
    });
  };

  updateAppLock = (appLockId, lockStatus) => {
    this.props
      .dispatch({
        type: 'app/updateAppLockStatus',
        payload: {
          appLockId,
          lockStatus,
        },
      })
      .then(() => {
        this.paginateAppLockList();
      });
  };

  updateLock = (e, text, record) => {
    this.updateAppLock(record.id, e ? 0 : 1);
  };

  handleChangeEnv = e => {
    this.setState(
      {
        envId: e.target.value,
        pageNum: 0,
      },
      () => this.paginateAppLockList()
    );
  };

  handlePage = (page, pageSize) => {
    this.setState(
      {
        pageNum: page - 1,
        pageSize,
      },
      () => this.paginateAppLockList()
    );
  };

  render() {
    const { app: { appLockList }, settings: { envList }, appLoading } = this.props;

    const lockSwitch = (text, record) => (
      <Switch
        checkedChildren={<Icon type="unlock" />}
        unCheckedChildren={<Icon type="lock" />}
        checked={text.value === 0}
        onChange={e => this.updateLock(e, text, record)}
      />
    );

    const columns = [
      {
        title: 'Name',
        dataIndex: 'app.name',
      },
      {
        title: 'Env',
        dataIndex: 'env.name',
      },
      {
        title: 'Update Time',
        dataIndex: 'updateTime',
      },
      {
        title: 'Operator',
        dataIndex: 'updateBy',
      },
      {
        title: '状态',
        dataIndex: 'lockStatus',
        render: (text, record) => {
          return lockSwitch(text, record);
        },
      },
    ];

    const extraContent = (
      <div>
        <Radio.Group defaultValue="0" onChange={this.handleChangeEnv}>
          <Radio.Button value="0">全部</Radio.Button>
          {(envList || []).map(env => <Radio.Button value={env.id}>{env.name}</Radio.Button>)}
        </Radio.Group>
      </div>
    );

    const paginationProps = {
      current: appLockList.number + 1,
      pageSize: appLockList.size,
      total: appLockList.totalElements,
      onChange: this.handlePage,
      showTotal: total => `Total ${total} items`,
    };

    return (
      <PageHeaderLayout>
        <Card bordered={false} title="应用列表" extra={extraContent}>
          <div className={styles.tableList}>
            <Table
              loading={appLoading}
              dataSource={appLockList.content}
              pagination={paginationProps}
              columns={columns}
              onChange={this.handleStandardTableChange}
              rowKey="id"
            />
          </div>
        </Card>
      </PageHeaderLayout>
    );
  }
}
