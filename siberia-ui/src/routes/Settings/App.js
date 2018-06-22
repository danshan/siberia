import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Tabs } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './App.less';

@connect(({ app, settings, loading }) => ({
  app,
  settings,
  appLoading: loading.models.app,
  settingsLoading: loading.models.settings,
}))
export default class App extends PureComponent {
  state = {
    selectedRows: [],
  };

  componentDidMount() {
    this.loadApp();
    this.findEnvList();
    this.findAppConfigList();
    this.findAppHostList();
  }

  loadApp = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'app/loadApp',
      payload: {
        appId: this.props.match.params.appId,
      },
    });
  };

  findEnvList = () => {
    this.props.dispatch({
      type: 'settings/findEnvList',
      payload: {
        count: 5,
      },
    });
  };

  findAppConfigList = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'app/findAppConfigList',
      payload: {
        appId: this.props.match.params.appId,
      },
    });
  };

  findAppHostList = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'app/findAppHostList',
      payload: {
        appId: this.props.match.params.appId,
      },
    });
  };

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  render() {
    const { app: { app, appHostList }, settings: { envList }, appLoading } = this.props;
    const { selectedRows } = this.state;

    const hostColumns = [];

    return (
      <PageHeaderLayout title="应用配置" content={app.module}>
        <Card name="appconfig" title="基础配置" className={styles.card} bordered={false}>
          <div>
            <Tabs defaultActiveKey="1">
              {(envList || []).map(env => {
                return (
                  <Tabs.TabPane tab={env.name} key={env.id}>
                    {env.name}
                  </Tabs.TabPane>
                );
              })}
            </Tabs>
          </div>
        </Card>

        <Card name="apphost" title="服务器配置" className={styles.card} bordered={false}>
          <div>
            <Tabs defaultActiveKey="1">
              {(envList || []).map(env => {
                return (
                  <Tabs.TabPane tab={env.name} key={env.id}>
                    <StandardTable
                      selectedRows={selectedRows}
                      loading={appLoading}
                      data={appHostList}
                      columns={hostColumns}
                      onSelectRow={this.handleSelectRows}
                      rowKey="id"
                    />
                  </Tabs.TabPane>
                );
              })}
            </Tabs>
          </div>
        </Card>
      </PageHeaderLayout>
    );
  }
}
