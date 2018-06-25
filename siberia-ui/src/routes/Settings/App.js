import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Tabs, Form, Input, Row, Col } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './App.less';

@connect(({ app, settings, loading }) => ({
  app,
  settings,
  appLoading: loading.models.app,
  settingsLoading: loading.models.settings,
}))
@Form.create()
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
    const { app: { app }, settings: { envList }, appLoading, form } = this.props;
    const { getFieldDecorator } = form;
    const { selectedRows } = this.state;

    const hostColumns = [];

    const envTab = env => (
      <Tabs.TabPane tab={env.name} key={env.id}>
        <Form layout="vertical" hideRequiredMark>
          <Row gutter={16}>
            <Col lg={6} md={12} sm={24}>
              <Form.Item label="端口号">
                {getFieldDecorator('name', {
                  rules: [{ required: true, message: '请输入端口号' }],
                })(<Input placeholder="请输入端口号" />)}
              </Form.Item>
            </Col>
            <Col xl={{ span: 16, offset: 2 }} lg={16} md={16} sm={24}>
              <Form.Item label="健康检查 Path">
                {getFieldDecorator('healthPath', {
                  rules: [{ required: true, message: '请输入健康检查 path' }],
                })(<Input placeholder="请输入健康检查 path" />)}
              </Form.Item>
            </Col>
          </Row>
        </Form>
      </Tabs.TabPane>
    );

    return (
      <PageHeaderLayout title="应用配置" content={app.module}>
        <Card name="appconfig" title="基础配置" className={styles.card} bordered={false}>
          <div>
            <Tabs defaultActiveKey="1">
              {(envList || []).map(env => {
                return envTab(env);
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
