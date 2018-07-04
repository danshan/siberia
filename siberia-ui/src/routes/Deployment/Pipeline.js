import { routerRedux } from 'dva/router';
import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import { Button, Badge, Card, Divider, Form, Input, message, Modal } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './Pipeline.less';

const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');

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

@connect(({ pipeline, loading }) => ({
  pipeline,
  loading: loading.models.pipeline,
}))
@Form.create()
export default class Pipeline extends PureComponent {
  state = {
    modalVisible: false,
    selectedRows: [],
    formValues: {},
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

  sendSuccessMessage = msg => {
    message.success(msg);
  };

  handleStandardTableChange = (pagination, filtersArg, sorter) => {
    const { formValues } = this.state;

    const filters = Object.keys(filtersArg).reduce((obj, key) => {
      const newObj = { ...obj };
      newObj[key] = getValue(filtersArg[key]);
      return newObj;
    }, {});

    const params = {
      currentPage: pagination.current,
      pageSize: pagination.pageSize,
      ...formValues,
      ...filters,
    };
    if (sorter.field) {
      params.sorter = `${sorter.field}_${sorter.order}`;
    }

    this.paginatePipelineDeploymentList();
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

  handleLog = record => {
    this.props.dispatch(routerRedux.push(`/deployment/logviewer/${record.id}`));
  };

  render() {
    const { pipeline: { pipeline, pipelineDeploymentList }, loading } = this.props;
    const { selectedRows, modalVisible } = this.state;

    const content = (
      <div className={styles.pageHeaderContent}>
        <p>{pipeline.description}</p>
      </div>
    );

    const columns = [
      {
        title: 'Project',
        dataIndex: 'project',
        sorter: true,
      },
      {
        title: 'Module',
        dataIndex: 'module',
        sorter: true,
      },
      {
        title: 'Build No.',
        dataIndex: 'buildNo',
        sorter: true,
        align: 'right',
      },
      {
        title: '更新时间',
        dataIndex: 'updateTime',
        sorter: true,
      },
      {
        title: '操作',
        render: (text, record) => (
          <Fragment>
            <a onClick={() => this.handleConfig(record)}>Config</a>
            <Divider type="vertical" />
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
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={pipelineDeploymentList}
              columns={columns}
              onSelectRow={this.handleSelectRows}
              onChange={this.handleStandardTableChange}
              rowKey="id"
            />
          </div>
        </Card>
        <CreateForm {...parentMethods} modalVisible={modalVisible} />
      </PageHeaderLayout>
    );
  }
}
