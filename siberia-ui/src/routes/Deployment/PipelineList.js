import React, { PureComponent } from 'react';
import moment from 'moment';
import { connect } from 'dva';
import { Link } from 'dva/router';
import {
  List,
  Card,
  Row,
  Col,
  Radio,
  Input,
  Button,
  Icon,
  Dropdown,
  Menu,
  Modal,
  Form,
} from 'antd';

import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './PipelineList.less';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;
const { Search } = Input;

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
    status: 0,
  };

  componentDidMount() {
    this.props.dispatch({
      type: 'pipeline/paginatePipelineList',
      payload: {
        count: 5,
        status: this.state.status,
      },
    });
  }

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
        <RadioGroup defaultValue="0">
          <RadioButton value="0">全部</RadioButton>
          <RadioButton value="1">进行中</RadioButton>
          <RadioButton value="2">已完成</RadioButton>
        </RadioGroup>
        <Search className={styles.extraContentSearch} placeholder="请输入" onSearch={() => ({})} />
      </div>
    );

    const paginationProps = {
      showSizeChanger: true,
      showQuickJumper: true,
      current: pipelineList.number,
      pageSize: pipelineList.size,
      total: pipelineList.totalElements,
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

    const menu = (
      <Menu>
        <Menu.Item>
          <a>编辑</a>
        </Menu.Item>
        <Menu.Item>
          <a>删除</a>
        </Menu.Item>
      </Menu>
    );

    const MoreBtn = () => (
      <Dropdown overlay={menu}>
        <a>
          更多 <Icon type="down" />
        </a>
      </Dropdown>
    );

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
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
            className={styles.listCard}
            bordered={false}
            title="标准列表"
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
                <List.Item actions={[<a>编辑</a>, <MoreBtn />]}>
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
