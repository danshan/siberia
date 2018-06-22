import { routerRedux } from 'dva/router';
import React, { PureComponent } from 'react';
import { Card, Form } from 'antd';
import { connect } from 'dva';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import EnvironmentForm from './EnvironmentForm';
import AppForm from './AppForm';
import styles from './style.less';

class Settings extends PureComponent {
  componentDidMount() {
    this.paginateEnvList();
    this.paginateAppList();
  }

  paginateEnvList = () => {
    this.props.dispatch({
      type: 'settings/paginateEnvList',
      payload: {
        count: 5,
      },
    });
  };

  paginateAppList = () => {
    this.props.dispatch({
      type: 'settings/paginateAppList',
      payload: {
        count: 5,
      },
    });
  };

  createEnv = env => {
    this.props.dispatch({
      type: 'settings/createEnv',
      payload: env,
    });
  };

  createApp = app => {
    this.props
      .dispatch({
        type: 'settings/createApp',
        payload: app,
      })
      .then(() => {
        this.paginateAppList();
      });
  };

  updateApp = app => {
    this.props
      .dispatch({
        type: 'settings/updateApp',
        payload: app,
      })
      .then(() => {
        this.paginateAppList();
      });
  };

  removeApp = appId => {
    this.props
      .dispatch({
        type: 'settings/removeApp',
        payload: {
          appId,
        },
      })
      .then(() => {
        this.paginateAppList();
      });
  };

  configApp = appId => {
    this.props.dispatch(routerRedux.push(`/settings/apps/${appId}`));
  };

  render() {
    const { settings: { envList, appList } } = this.props;

    return (
      <PageHeaderLayout title="管理后台" content="管理后台" wrapperClassName={styles.advancedForm}>
        <Card name="environment" title="环境管理" className={styles.card} bordered={false}>
          <EnvironmentForm envList={envList} createEnv={this.createEnv} />
        </Card>

        <Card name="app" title="应用配置" className={styles.card} bordered={false}>
          <AppForm
            envList={appList}
            create={this.createApp}
            update={this.updateApp}
            remove={this.removeApp}
            config={this.configApp}
          />
        </Card>
      </PageHeaderLayout>
    );
  }
}

export default connect(({ settings, loading }) => ({
  settings,
  loading: loading.models.settings,
}))(Form.create()(Settings));
