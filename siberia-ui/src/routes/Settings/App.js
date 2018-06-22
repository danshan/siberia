import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './App.less';

@connect(({ app, loading }) => ({
  app,
  loading: loading.models.app,
}))
export default class App extends PureComponent {
  state = {
    selectedRows: [],
  };

  componentDidMount() {
    this.loadApp();
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
    const { app: { app, appHostList }, loading } = this.props;
    const { selectedRows } = this.state;

    const hostColumns = [];

    return (
      <PageHeaderLayout title="应用配置" content={app.module}>
        <Card name="appconfig" title="基础配置" className={styles.card} bordered={false}>
          <div />
        </Card>

        <Card name="apphost" title="服务器配置" className={styles.card} bordered={false}>
          <div>
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={appHostList}
              columns={hostColumns}
              onSelectRow={this.handleSelectRows}
              rowKey="id"
            />
          </div>
        </Card>
      </PageHeaderLayout>
    );
  }
}
