import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Button, Card, Input, message, Tabs } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './App.less';

@connect(({ app, settings, loading }) => ({
  app,
  settings,
  appLoading: loading.models.app,
  settingsLoading: loading.models.settings,
}))
export default class App extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      app: props.app,
      settings: props.settings,
    };
  }

  componentDidMount() {
    this.loadApp();
    this.findEnvList();
    this.findAppConfigList();
    this.findAppHostList();
  }

  componentWillReceiveProps(nextProps) {
    if ('app' in nextProps) {
      this.setState({ app: nextProps.app });
    }

    if ('settings' in nextProps) {
      this.setState({ settings: nextProps.settings });
    }
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
      payload: {},
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

  updateAppConfig = (env, config) => {
    this.props
      .dispatch({
        type: 'app/updateAppConfig',
        payload: {
          appId: this.props.match.params.appId,
          envId: env.id,
          content: JSON.parse(config),
        },
      })
      .then(() => {
        message.success('修改成功');
        this.loadApp();
      });
  };

  updateAppHost = (env, host) => {
    this.props
      .dispatch({
        type: 'app/updateAppHost',
        payload: {
          appId: this.props.match.params.appId,
          envId: env.id,
          hosts: JSON.parse(host),
        },
      })
      .then(() => {
        message.success('修改成功');
        this.loadApp();
      });
  };

  handleChangeConfig = (e, env) => {
    const stateApp = this.state.app;
    const configs = stateApp.appConfigMap;
    configs[String(env.id)] = e.target.value;
    this.setState({
      app: {
        ...stateApp,
        appConfigMap: configs,
      },
    });
  };

  handleChangeHost = (e, env) => {
    const stateApp = this.state.app;
    const hosts = stateApp.appHostMap;
    hosts[String(env.id)] = e.target.value;
    this.setState({
      app: {
        ...stateApp,
        appHostMap: hosts,
      },
    });
  };

  handleSaveConfig = (e, env) => {
    const config = this.state.app.appConfigMap[String(env.id)];
    this.updateAppConfig(env, config);
  };

  handleSaveHost = (e, env) => {
    const host = this.state.app.appHostMap[String(env.id)];
    this.updateAppHost(env, host);
  };

  render() {
    const { appLoading, settingsLoading } = this.props;
    const { app: { appConfigMap, appHostMap }, settings: { envList } } = this.state;

    const envConfigTab = env => {
      return (
        <Tabs.TabPane tab={env.name} key={env.id}>
          {appConfigMap && (
            <div>
              <Input.TextArea
                placeholder="Input config content"
                autosize={{ minRows: 2, maxRows: 6 }}
                value={appConfigMap[String(env.id)]}
                onChange={e => this.handleChangeConfig(e, env)}
              />
              <Button
                type="primary"
                icon="save"
                loading={settingsLoading}
                onClick={e => this.handleSaveConfig(e, env)}
              >
                Save
              </Button>
            </div>
          )}
        </Tabs.TabPane>
      );
    };

    const envHostTab = env => {
      return (
        <Tabs.TabPane tab={env.name} key={env.id}>
          {appHostMap && (
            <div>
              <Input.TextArea
                placeholder="Input app hosts"
                autosize={{ minRows: 2, maxRows: 6 }}
                value={appHostMap[String(env.id)]}
                onChange={e => this.handleChangeHost(e, env)}
              />
              <Button
                type="primary"
                icon="save"
                loading={appLoading}
                onClick={e => this.handleSaveHost(e, env)}
              >
                Save
              </Button>
            </div>
          )}
        </Tabs.TabPane>
      );
    };

    return (
      <PageHeaderLayout title="应用配置">
        <Card name="appconfig" title="基础配置" className={styles.card} bordered={false}>
          <Tabs defaultActiveKey="1">
            {(envList || []).map(env => {
              return envConfigTab(env);
            })}
          </Tabs>
        </Card>

        <Card name="apphost" title="服务器配置" className={styles.card} bordered={false}>
          <Tabs defaultActiveKey="1">
            {(envList || []).map(env => {
              return envHostTab(env);
            })}
          </Tabs>
        </Card>
      </PageHeaderLayout>
    );
  }
}
