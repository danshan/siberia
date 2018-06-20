import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Switch, Icon, Radio } from 'antd';

import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './AppConfigList.less';

@connect(({ app, loading }) => ({
  app,
  loading: loading.models.pipeline,
}))
export default class AppConfigList extends PureComponent {
  state = {
    selectedRows: [],
  };

  componentDidMount() {
    this.paginateAppLockList();
    this.paginateEnvList();
  }

  paginateAppLockList = () => {
    this.props.dispatch({
      type: 'app/paginateAppLockList',
      payload: {
        count: 5,
      },
    });
  };

  paginateEnvList = () => {
    this.props.dispatch({
      type: 'app/paginateEnvList',
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

  render() {
    const { app: { envList, appLockList }, loading } = this.props;
    const { selectedRows } = this.state;

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
        title: 'Project',
        dataIndex: 'project',
      },
      {
        title: 'Module',
        dataIndex: 'module',
      },
      {
        title: '状态',
        dataIndex: 'lockStatus',
        render: (text, record) => {
          return lockSwitch(text, record);
        },
      },
    ];

    const appFilter = (
      <div>
        <Radio.Group defaultValue="0">
          {(envList || []).map(env => {
            return (
              <Radio.Button key={env.id} value={env.id}>
                {env.name}
              </Radio.Button>
            );
          })}
        </Radio.Group>
      </div>
    );

    return (
      <PageHeaderLayout>
        <Card bordered={false} title="应用列表" extra={appFilter}>
          <div className={styles.tableList}>
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={appLockList}
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
