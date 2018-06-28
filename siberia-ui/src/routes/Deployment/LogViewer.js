import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Card } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './LogViewer.less';

@connect(({ logviewer, loading }) => ({
  logviewer,
  loading: loading.models.logviewer,
}))
export default class LogViewer extends PureComponent {
  componentDidMount() {
    this.findSiberiaLogs();
  }

  findSiberiaLogs = () => {
    this.props.dispatch({
      type: 'logviewer/findSiberiaLogs',
      payload: {
        id: this.props.match.params.taskId,
      },
    });
  };

  render() {
    const { logviewer: { siberiaLogs } } = this.props;

    const renderSiberiaLogs = () => {
      const parseLog = log => {
        return `${log.createTime} ${log.result.value} ${log.step} ${log.detail}`;
      };
      const collectLine = (logs, line) => {
        return `${logs + line}\n`;
      };

      const logs = (siberiaLogs || []).map(log => parseLog(log)).reduce(collectLine, '');
      return logs;
    };

    return (
      <PageHeaderLayout>
        <Card title="Siberia Log" className={styles.card} bordered={false}>
          <div className={styles.logHighlight}>
            <pre>{`${renderSiberiaLogs()}`}</pre>
          </div>
        </Card>
      </PageHeaderLayout>
    );
  }
}
