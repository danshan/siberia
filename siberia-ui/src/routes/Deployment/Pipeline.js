import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import {
  Badge,
  Button,
  Card,
  Col,
  DatePicker,
  Divider,
  Dropdown,
  Form,
  Icon,
  Input,
  InputNumber,
  Menu,
  message,
  Modal,
  Row,
  Select,
} from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './Pipeline.less';

const { Option } = Select;
const getValue = obj =>
  Object.keys(obj)
    .map(key => obj[key])
    .join(',');
const statusMap = ['default', 'processing', 'success', 'error'];
const status = ['关闭', '运行中', '已上线', '异常'];

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
export default class TableList extends PureComponent {
  state = {
    modalVisible: false,
    expandForm: false,
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
    });
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

  handleFormReset = () => {
    const { form } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    this.paginatePipelineDeploymentList();
  };

  toggleForm = () => {
    this.setState({
      expandForm: !this.state.expandForm,
    });
  };

  handleMenuClick = e => {
    const { dispatch } = this.props;
    const { selectedRows } = this.state;

    if (!selectedRows) return;

    switch (e.key) {
      case 'remove':
        dispatch({
          type: 'rule/remove',
          payload: {
            no: selectedRows.map(row => row.no).join(','),
          },
          callback: () => {
            this.setState({
              selectedRows: [],
            });
          },
        });
        break;
      default:
        break;
    }
  };

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  handleSearch = e => {
    e.preventDefault();

    const { dispatch, form } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;

      const values = {
        ...fieldsValue,
        updatedAt: fieldsValue.updatedAt && fieldsValue.updatedAt.valueOf(),
      };

      this.setState({
        formValues: values,
      });

      dispatch({
        type: 'rule/fetch',
        payload: values,
      });
    });
  };

  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };

  handleAdd = fields => {
    this.createPipelineDeployment(fields);

    message.success('添加成功');
    this.setState({
      modalVisible: false,
    });

    this.paginatePipelineDeploymentList();
  };

  renderSimpleForm() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <Form.Item label="规则编号">
              {getFieldDecorator('no')(<Input placeholder="请输入" />)}
            </Form.Item>
          </Col>
          <Col md={8} sm={24}>
            <Form.Item label="使用状态">
              {getFieldDecorator('status')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">关闭</Option>
                  <Option value="1">运行中</Option>
                </Select>
              )}
            </Form.Item>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
                重置
              </Button>
              <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
                展开 <Icon type="down" />
              </a>
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  renderAdvancedForm() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <Form.Item label="规则编号">
              {getFieldDecorator('no')(<Input placeholder="请输入" />)}
            </Form.Item>
          </Col>
          <Col md={8} sm={24}>
            <Form.Item label="使用状态">
              {getFieldDecorator('status')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">关闭</Option>
                  <Option value="1">运行中</Option>
                </Select>
              )}
            </Form.Item>
          </Col>
          <Col md={8} sm={24}>
            <Form.Item label="调用次数">
              {getFieldDecorator('number')(<InputNumber style={{ width: '100%' }} />)}
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <Form.Item label="更新日期">
              {getFieldDecorator('date')(
                <DatePicker style={{ width: '100%' }} placeholder="请输入更新日期" />
              )}
            </Form.Item>
          </Col>
          <Col md={8} sm={24}>
            <Form.Item label="使用状态">
              {getFieldDecorator('status3')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">关闭</Option>
                  <Option value="1">运行中</Option>
                </Select>
              )}
            </Form.Item>
          </Col>
          <Col md={8} sm={24}>
            <Form.Item label="使用状态">
              {getFieldDecorator('status4')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">关闭</Option>
                  <Option value="1">运行中</Option>
                </Select>
              )}
            </Form.Item>
          </Col>
        </Row>
        <div style={{ overflow: 'hidden' }}>
          <span style={{ float: 'right', marginBottom: 24 }}>
            <Button type="primary" htmlType="submit">
              查询
            </Button>
            <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>
              重置
            </Button>
            <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
              收起 <Icon type="up" />
            </a>
          </span>
        </div>
      </Form>
    );
  }

  renderForm() {
    return this.state.expandForm ? this.renderAdvancedForm() : this.renderSimpleForm();
  }

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
        title: '状态',
        dataIndex: 'status',
        filters: [
          {
            text: status[0],
            value: 0,
          },
          {
            text: status[1],
            value: 1,
          },
          {
            text: status[2],
            value: 2,
          },
          {
            text: status[3],
            value: 3,
          },
        ],
        onFilter: (value, record) => record.status.toString() === value,
        render(val) {
          return <Badge status={statusMap[val]} text={status[val]} />;
        },
      },
      {
        title: '更新时间',
        dataIndex: 'updateTime',
        sorter: true,
      },
      {
        title: '操作',
        render: () => (
          <Fragment>
            <a href="">Log</a>
            <Divider type="vertical" />
            <a href="">Config</a>
            <Divider type="vertical" />
            <Dropdown overlay={menu}>
              <Button>
                Deploy <Icon type="down" />
              </Button>
            </Dropdown>
          </Fragment>
        ),
      },
    ];

    const menu = (
      <Menu onClick={this.handleMenuClick} selectedKeys={[]}>
        <Menu.Item key="remove">删除</Menu.Item>
        <Menu.Item key="approval">批量审批</Menu.Item>
      </Menu>
    );

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };

    return (
      <PageHeaderLayout title={pipeline.title} content={content}>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListForm}>{this.renderForm()}</div>
            <div className={styles.tableListOperator}>
              <Button icon="plus" type="primary" onClick={() => this.handleModalVisible(true)}>
                新建
              </Button>
              {selectedRows.length > 0 && (
                <span>
                  <Button>批量操作</Button>
                  <Dropdown overlay={menu}>
                    <Button>
                      更多操作 <Icon type="down" />
                    </Button>
                  </Dropdown>
                </span>
              )}
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
