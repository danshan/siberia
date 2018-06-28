import React, { Fragment, PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Row, Col, Radio, Input, Divider, Badge } from 'antd';
import StandardTable from 'components/StandardTable';
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
    selectedRows: [],
  };

  componentDidMount() {
    this.props.dispatch({
      type: 'task/paginateTaskList',
      payload: {
        count: 5,
      },
    });
  }

  handleSelectRows = rows => {
    this.setState({
      selectedRows: rows,
    });
  };

  render() {
    const { task: { taskList }, loading } = this.props;
    const { selectedRows } = this.state;

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
        sorter: true,
        render: (text, record) => {
          return (
            <p>
              {record.project || ''} : {record.module}
            </p>
          );
        },
      },
      {
        title: 'Build No.',
        dataIndex: 'buildNo',
        sorter: true,
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
        render: () => (
          <Fragment>
            <a href="">Log</a>
            <Divider type="vertical" />
            <a href="">重新发布</a>
          </Fragment>
        ),
      },
    ];

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
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={taskList}
              columns={columns}
              onSelectRow={this.handleSelectRows}
              rowKey="id"
            />
          </Card>
        </div>
      </PageHeaderLayout>
    );
  }
}
