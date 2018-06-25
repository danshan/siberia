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
    this.findEnvList();
    this.paginateAppList();
  }

  findEnvList = () => {
    this.props.dispatch({
      type: 'settings/findEnvList',
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
    this.props
      .dispatch({
        type: 'settings/createEnv',
        payload: env,
      })
      .then(() => {
        this.findEnvList();
      });
  };

  updateEnv = env => {
    this.props
      .dispatch({
        type: 'settings/updateEnv',
        payload: env,
      })
      .then(() => {
        this.findEnvList();
      });
  };

  createApp = app => {
    const req = {
      project: app.project,
      module: app.module,
      appType: app.appType.value,
    };
    this.props
      .dispatch({
        type: 'settings/createApp',
        payload: req,
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

  updateEnv = env => {
    this.props
      .dispatch({
        type: 'settings/updateEnv',
        payload: env,
      })
      .then(() => {
        this.findEnvList();
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

  removeEnv = envId => {
    this.props
      .dispatch({
        type: 'settings/removeEnv',
        payload: {
          envId,
        },
      })
      .then(() => {
        this.findEnvList();
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
          <EnvironmentForm
            envList={envList}
            create={this.createEnv}
            update={this.updateEnv}
            remove={this.removeEnv}
          />
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
