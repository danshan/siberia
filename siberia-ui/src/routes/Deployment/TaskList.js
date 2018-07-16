import React, { Fragment, PureComponent } from 'react';
import SockJsClient from 'react-stomp';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import { Card, Row, Col, Radio, Input, Divider, Badge, Table } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './TaskList.less';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;
const { Search } = Input;

@connect(({ task, loading }) => ({
  task,
  loading: loading.models.task,
}))
export default class TaskList extends PureComponent {
  state = {
    pageNum: 0,
    pageSize: 20,
  };

  componentDidMount() {
    this.paginateTaskList();
  }

  paginateTaskList = () => {
    this.props.dispatch({
      type: 'task/paginateTaskList',
      payload: {
        pageNum: this.state.pageNum,
        pageSize: this.state.pageSize,
      },
    });
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
      () => this.paginateTaskList()
    );
  };

  handleEvent = msg => {
    this.props.dispatch({
      type: 'task/refreshTask',
      payload: msg,
    });
  };

  handleRollback = record => {
    this.props.dispatch({
      type: 'task/rollbackTask',
      payload: {
        taskId: record.id,
      },
    });
  };

  render() {
    const { task: { taskList }, loading } = this.props;

    const Info = ({ title, value, bordered }) => (
      <div className={styles.headerInfo}>
        <span>{title}</span>
        <p>{value}</p>
        {bordered && <em />}
      </div>
    );

    const extraContent = (
      <div className={styles.extraContent}>
        <RadioGroup defaultValue="all">
          <RadioButton value="all">全部</RadioButton>
          <RadioButton value="progress">进行中</RadioButton>
          <RadioButton value="done">已完成</RadioButton>
        </RadioGroup>
        <Search className={styles.extraContentSearch} placeholder="请输入" onSearch={() => ({})} />
      </div>
    );

    const columns = [
      {
        title: 'App',
        dataIndex: 'module',
        render: (module, record) => {
          if (module) {
            return `${record.project}:${record.module}`;
          } else {
            return `${record.project}`;
          }
        },
      },
      {
        title: 'Build No.',
        dataIndex: 'buildNo',
      },
      {
        title: 'Env',
        dataIndex: 'env.name',
      },
      {
        title: 'Status',
        dataIndex: 'status',
        render: status => {
          if (status.value === 'SERVICING' || status.value === 'RUNNING') {
            return <Badge status="default" text={status.desc} />;
          } else if (status.value === 'FAIL') {
            return <Badge status="error" text={status.desc} />;
          } else if (status.value === 'OK') {
            return <Badge status="success" text={status.desc} />;
          } else if (status.value === 'CREATED' || status.value === 'ROLLBACK') {
            return <Badge status="warning" text={status.desc} />;
          } else {
            return <Badge status="default" text={status.desc} />;
          }
        },
      },
      {
        title: '操作',
        render: record => (
          <Fragment>
            <a onClick={() => this.handleLog(record)}>Log</a>
            <Divider type="vertical" />
            <a href="">重新发布</a>
            <Divider type="vertical" />
            <a onClick={() => this.handleRollback(record)}>回滚</a>
          </Fragment>
        ),
      },
    ];

    const paginationProps = {
      current: taskList.number + 1,
      pageSize: taskList.size,
      total: taskList.totalElements,
      onChange: this.handlePage,
      showTotal: total => `Total ${total} items`,
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
            title="任务列表"
            style={{ marginTop: 24 }}
            bodyStyle={{ padding: '0 32px 40px 32px' }}
            extra={extraContent}
          >
            <Table
              loading={loading}
              dataSource={taskList.content}
              pagination={paginationProps}
              columns={columns}
              rowKey="id"
            />
          </Card>
          <SockJsClient
            url="http://localhost:8080/wsendpoint"
            topics={['/task']}
            onMessage={msg => this.handleEvent(msg)}
          />
        </div>
      </PageHeaderLayout>
    );
  }
}
