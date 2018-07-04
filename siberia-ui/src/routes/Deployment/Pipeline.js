import { routerRedux } from 'dva/router';
import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import { Button, Badge, Card, Divider, Form, Input, message, Modal, Table } from 'antd';

import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './Pipeline.less';

const CreateForm = Form.create()(props => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      title="Create Deployment"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <Form.Item labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="Project">
        {form.getFieldDecorator('project', {
          rules: [{ required: true, message: 'Please input project name...' }],
        })(<Input placeholder="project name" />)}
      </Form.Item>
      <Form.Item labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="Module">
        {form.getFieldDecorator('module', {
          rules: [{ required: true, message: 'Please input module name...' }],
        })(<Input placeholder="module name" />)}
      </Form.Item>
      <Form.Item labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="Build No.">
        {form.getFieldDecorator('buildNo', {
          rules: [{ required: true, message: 'Please input build NO.' }],
        })(<Input placeholder="build number" />)}
      </Form.Item>
    </Modal>
  );
});

@connect(({ pipeline, task, loading }) => ({
  pipeline,
  task,
  loading: loading.models.pipeline,
}))
@Form.create()
export default class Pipeline extends PureComponent {
  state = {
    modalVisible: false,

    pageNum: 0,
    pageSize: 20,
  };

  componentDidMount() {
    this.loadPipeline();
    this.paginatePipelineDeploymentList();
  }

  loadPipeline = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'pipeline/loadPipeline',
      payload: {
        pipelineId: this.props.match.params.pipelineId,
      },
    });
  };

  paginatePipelineDeploymentList = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'pipeline/paginatePipelineDeploymentList',
      payload: {
        pipelineId: this.props.match.params.pipelineId,
        pageNum: this.state.pageNum,
        pageSize: this.state.pageSize,
      },
    });
  };

  createPipelineDeployment = fields => {
    const { dispatch } = this.props;
    dispatch({
      type: 'pipeline/createPipelineDeployment',
      payload: {
        pipelineId: this.props.match.params.pipelineId,
        project: fields.project,
        module: fields.module,
        buildNo: fields.buildNo,
      },
    }).then(() => {
      this.sendSuccessMessage('添加成功');
      this.setState({
        modalVisible: false,
      });
      this.paginatePipelineDeploymentList();
    });
  };

  createTask = deployment => {
    this.props.dispatch({
      type: 'task/createTask',
      payload: {
        envId: 1,
        deploymentId: deployment.id,
      },
    });
  };

  sendSuccessMessage = msg => {
    message.success(msg);
  };

  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };

  handleAdd = fields => {
    this.createPipelineDeployment(fields);
  };

  handleConfig = record => {
    this.props.dispatch(routerRedux.push(`/settings/apps/${record.app.id}`));
  };

  handleCreateTask = record => {
    console.log(record);
    this.createTask(record);
  };

  handleLog = record => {
    this.props.dispatch(routerRedux.push(`/deployment/logviewer/${record.id}`));
  };

  handlePage = (page, pageSize) => {
    this.setState(
      {
        pageNum: page - 1,
        pageSize,
      },
      () => this.paginatePipelineDeploymentList()
    );
  };

  render() {
    const { pipeline: { pipeline, pipelineDeploymentList }, loading } = this.props;
    const { modalVisible } = this.state;

    const content = (
      <div className={styles.pageHeaderContent}>
        <p>{pipeline.description}</p>
      </div>
    );

    const paginationProps = {
      current: pipelineDeploymentList.number + 1,
      pageSize: pipelineDeploymentList.size,
      total: pipelineDeploymentList.totalElements,
      onChange: this.handlePage,
      showTotal: total => `Total ${total} items`,
    };

    const columns = [
      {
        title: 'Name',
        dataIndex: 'app.name',
      },
      {
        title: 'Build No.',
        dataIndex: 'buildNo',
        align: 'right',
      },
      {
        title: '更新时间',
        dataIndex: 'updateTime',
      },
      {
        title: '操作',
        render: (text, record) => (
          <Fragment>
            <a onClick={() => this.handleConfig(record)}>Config</a>
            <Divider type="vertical" />
            <a onClick={() => this.handleCreateTask(record)}>Deploy</a>
            <Button.Group>
              {[].map(config => {
                return (
                  <Button key={config.env.id}>
                    <Badge status="error" />
                    {config.env.name}
                  </Button>
                );
              })}
            </Button.Group>
          </Fragment>
        ),
      },
    ];

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };

    return (
      <PageHeaderLayout title={pipeline.title} content={content}>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                新建
              </Button>
            </div>
            <Table
              loading={loading}
              pagination={paginationProps}
              columns={columns}
              dataSource={pipelineDeploymentList.content}
              rowKey="id"
            />
          </div>
        </Card>
        <CreateForm {...parentMethods} modalVisible={modalVisible} />
      </PageHeaderLayout>
    );
  }
}
