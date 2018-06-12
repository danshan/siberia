import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card, Badge } from 'antd';

import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './AppConfigList.less';

@connect(({ app, loading }) => ({
  app,
  loading: loading.models.pipeline,
}))
export default class AppConfigList extends PureComponent {
  state = {
    selectedRows: [],
  };

  componentDidMount() {
    this.props.dispatch({
      type: 'app/paginateAppLockList',
      payload: {
        count: 5,
      },
    });
  }

  render() {
    const { app: { appLockList }, loading } = this.props;
    const { selectedRows } = this.state;

    const status = ['UNLOKCED', 'LOCKED'];
    const columns = [
      {
        title: 'Project',
        dataIndex: 'project',
      },
      {
        title: 'Module',
        dataIndex: 'module',
      },
      {
        title: '状态',
        dataIndex: 'lockStatus',
        filters: [
          {
            text: status[0],
            value: 0,
          },
          {
            text: status[1],
            value: 1,
          },
        ],
        onFilter: (value, record) => record.lockStatus.value === value,
        render(val) {
          return <Badge status={status[val.value]} text={status[val.value]} />;
        },
      },
    ];

    return (
      <PageHeaderLayout>
        <Card bordered={false}>
          <div className={styles.tableList}>
            <StandardTable
              selectedRows={selectedRows}
              loading={loading}
              data={appLockList}
              columns={columns}
              onChange={this.handleStandardTableChange}
              rowKey="id"
            />
          </div>
        </Card>
      </PageHeaderLayout>
    );
  }
}
