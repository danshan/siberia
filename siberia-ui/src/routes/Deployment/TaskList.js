import React, { PureComponent } from 'react';
import moment from 'moment';
import { connect } from 'dva';
import { Link } from 'dva/router';
import { List, Card, Row, Col, Radio, Input, Progress, Icon, Dropdown, Menu } from 'antd';

import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './PipelineList.less';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;
const { Search } = Input;

@connect(({ task, loading }) => ({
  task,
  loading: loading.models.pipeline,
}))
export default class TaskList extends PureComponent {
  componentDidMount() {
    this.props.dispatch({
      type: 'task/paginateTaskList',
      payload: {
        count: 5,
      },
    });
  }

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

    const paginationProps = {
      showSizeChanger: true,
      showQuickJumper: true,
      pageSize: taskList.data.size,
      total: taskList.data.total,
    };

    const ListContent = ({ data: { createBy, createTime, percent, status } }) => (
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
          <Progress percent={percent} status={status} strokeWidth={6} style={{ width: 180 }} />
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
            title="任务列表"
            style={{ marginTop: 24 }}
            bodyStyle={{ padding: '0 32px 40px 32px' }}
            extra={extraContent}
          >
            <List
              size="large"
              rowKey="id"
              loading={loading}
              pagination={paginationProps}
              dataSource={taskList.data.list}
              renderItem={item => (
                <List.Item actions={[<a>编辑</a>, <MoreBtn />]}>
                  <List.Item.Meta
                    title={
                      <Link to={`tasks/${item.id}`}>
                        {item.project} - {item.module}
                      </Link>
                    }
                    description={item.description}
                  />
                  <ListContent data={item} />
                </List.Item>
              )}
            />
          </Card>
        </div>
      </PageHeaderLayout>
    );
  }
}
