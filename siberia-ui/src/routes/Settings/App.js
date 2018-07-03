import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Button, Card, Input, Tabs, message } from 'antd';
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
  constructor(props) {
    super(props);

    this.state = {
      app: props.app,
      settings: props.settings,
      selectedRows: [],
    };
  }

  componentDidMount() {
    this.loadApp();
    this.findEnvList();
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

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  handleChangeConfig = (e, env) => {
    const configs = this.state.app.appConfigMap;
    configs[String(env.id)] = e.target.value;
    this.setState({
      app: {
        appConfigMap: configs,
      },
    });
  };

  handleSaveConfig = (e, env) => {
    const config = this.state.app.appConfigMap[String(env.id)];
    this.updateAppConfig(env, config);
  };

  render() {
    const { appLoading } = this.props;
    const { app: { appConfigMap }, settings: { envList }, selectedRows } = this.state;

    const hostColumns = [];

    const envTab = env => {
      return (
        <Tabs.TabPane tab={env.name} key={env.id}>
          <Input.TextArea
            placeholder="Input config content"
            autosize={{ minRows: 2, maxRows: 6 }}
            value={appConfigMap[String(env.id)]}
            onChange={e => this.handleChangeConfig(e, env)}
          />
          <Button
            type="primary"
            icon="save"
            loading={this.state.iconLoading}
            onClick={e => this.handleSaveConfig(e, env)}
          >
            Save
          </Button>
        </Tabs.TabPane>
      );
    };

    return (
      <PageHeaderLayout title="应用配置">
        <Card name="appconfig" title="基础配置" className={styles.card} bordered={false}>
          <Tabs defaultActiveKey="1">
            {(envList || []).map(env => {
              return envTab(env);
            })}
          </Tabs>
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
                      data={[]}
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
