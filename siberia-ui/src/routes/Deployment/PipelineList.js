import React, { PureComponent } from 'react';
import moment from 'moment';
import { connect } from 'dva';
import { Link } from 'dva/router';
import { List, Card, Row, Col, Radio, Input, Button, Modal, Form } from 'antd';

import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './PipelineList.less';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

const CreateForm = Form.create()(props => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      handleAdd(fieldsValue);
      handleModalVisible();
      form.resetFields();
    });
  };
  return (
    <Modal
      title="新建发布流水线"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <Form.Item labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="标题">
        {form.getFieldDecorator('title', {
          rules: [{ required: true, message: 'Please input pipeline title...' }],
        })(<Input placeholder="请输入标题" />)}
      </Form.Item>
      <Form.Item labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="描述">
        {form.getFieldDecorator('description', {
          rules: [{ required: true, message: 'Please input some description...' }],
        })(<Input placeholder="请输入描述" />)}
      </Form.Item>
    </Modal>
  );
});

@connect(({ pipeline, loading }) => ({
  pipeline,
  loading: loading.models.pipeline,
}))
export default class PipelineList extends PureComponent {
  state = {
    modalVisible: false,

    status: 1,
    pageNum: 0,
    pageSize: 20,
  };

  componentDidMount() {
    this.pagiantePipelineList(this.state.pageNum, this.state.pageSize, this.state.status);
  }

  pagiantePipelineList = (pageNum, pageSize, status) => {
    this.props.dispatch({
      type: 'pipeline/paginatePipelineList',
      payload: {
        pageNum,
        pageSize,
        status,
      },
    });
  };

  updatePipelineStatus = (pipelineId, status) => {
    this.props
      .dispatch({
        type: 'pipeline/updatePipelineStatus',
        payload: {
          pipelineId,
          status,
        },
      })
      .then(() =>
        this.pagiantePipelineList(this.state.pageNum, this.state.pageSize, this.state.status)
      );
  };

  handleModalVisible = flag => {
    this.setState({
      modalVisible: !!flag,
    });
  };

  handleAdd = fieldValues => {
    this.props.dispatch({
      type: 'pipeline/createPipeline',
      payload: fieldValues,
    });
  };

  handlePage = (page, pageSize) => {
    this.setState(
      {
        pageNum: page - 1,
        pageSize,
      },
      () => this.pagiantePipelineList(this.state.pageNum, this.state.pageSize, this.state.status)
    );
  };

  handleFilterStatus = e => {
    this.setState({ status: e.target.value }, () =>
      this.pagiantePipelineList(this.state.pageNum, this.state.pageSize, this.state.status)
    );
  };

  handleUpdateStatus = (record, status) => {
    this.updatePipelineStatus(record.id, status);
  };

  render() {
    const { pipeline: { pipelineList }, loading } = this.props;
    const { modalVisible } = this.state;

    const Info = ({ title, value, bordered }) => (
      <div className={styles.headerInfo}>
        <span>{title}</span>
        <p>{value}</p>
        {bordered && <em />}
      </div>
    );

    const extraContent = (
      <div>
        <RadioGroup defaultValue={String(this.state.status)} onChange={this.handleFilterStatus}>
          <RadioButton value="0">全部</RadioButton>
          <RadioButton value="1">进行中</RadioButton>
          <RadioButton value="2">已归档</RadioButton>
        </RadioGroup>
      </div>
    );

    const paginationProps = {
      current: pipelineList.number + 1,
      pageSize: pipelineList.size,
      total: pipelineList.totalElements,
      onChange: this.handlePage,
      showTotal: total => `Total ${total} items`,
    };

    const ListContent = ({ data: { createBy, createTime, status } }) => (
      <div className={styles.listContent}>
        <div className={styles.listContentItem}>
          <span>Owner</span>
          <p>{createBy}</p>
        </div>
        <div className={styles.listContentItem}>
          <span>开始时间</span>
          <p>{moment(createTime).format('YYYY-MM-DD HH:mm')}</p>
        </div>
        <div className={styles.listContentItem}>
          <p>{status.desc}</p>
        </div>
      </div>
    );

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };

    const operations = item => {
      let op = null;
      if (item.status.value === 1) {
        op = [<a>编辑</a>, <a onClick={() => this.handleUpdateStatus(item, 2)}>归档</a>];
      } else if (item.status.value === 2) {
        op = [<a>编辑</a>, <a onClick={() => this.handleUpdateStatus(item, 1)}>执行</a>];
      }
      return op;
    };

    return (
      <PageHeaderLayout>
        <div className={styles.standardList}>
          <Card bordered={false}>
            <Row>
              <Col sm={8} xs={24}>
                <Info title="我的待办" value="8个任务" bordered />
              </Col>
              <Col sm={8} xs={24}>
                <Info title="本周任务平均处理时间" value="32分钟" bordered />
              </Col>
              <Col sm={8} xs={24}>
                <Info title="本周完成任务数" value="24个任务" />
              </Col>
            </Row>
          </Card>

          <Card
            bordered={false}
            title="发布流水线"
            style={{ marginTop: 24 }}
            bodyStyle={{ padding: '0 32px 40px 32px' }}
            extra={extraContent}
          >
            <Button
              type="dashed"
              onClick={() => this.handleModalVisible(true)}
              style={{ width: '100%', marginBottom: 8 }}
              icon="plus"
            >
              新建
            </Button>
            <List
              size="large"
              rowKey="id"
              loading={loading}
              pagination={paginationProps}
              dataSource={pipelineList.content}
              renderItem={item => (
                <List.Item actions={operations(item)}>
                  <List.Item.Meta
                    title={<Link to={`pipelines/${item.id}`}>{item.title}</Link>}
                    description={item.description}
                  />
                  <ListContent data={item} />
                </List.Item>
              )}
            />
          </Card>
          <CreateForm {...parentMethods} modalVisible={modalVisible} />
        </div>
      </PageHeaderLayout>
    );
  }
}
