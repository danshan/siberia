import React, { PureComponent } from 'react';
import SockJsClient from 'react-stomp';
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
    this.findAnsibleLogs();
  }

  findSiberiaLogs = () => {
    this.props.dispatch({
      type: 'logviewer/findSiberiaLogs',
      payload: {
        id: this.props.match.params.taskId,
      },
    });
  };

  findAnsibleLogs = () => {
    this.props.dispatch({
      type: 'logviewer/findAnsibleLogs',
      payload: {
        id: this.props.match.params.taskId,
      },
    });
  };

  pushSiberiaLog = msg => {
    this.props.dispatch({
      type: 'logviewer/pushSiberiaLog',
      payload: msg,
    });
  };

  pushAnsibleLog = msg => {
    console.log(msg);
    this.props.dispatch({
      type: 'logviewer/pushAnsibleLog',
      payload: msg,
    });
  };

  render() {
    const { logviewer: { siberiaLogs, ansibleLogs } } = this.props;

    const renderSiberiaLogs = () => {
      let index = 0;
      const parseLog = log => {
        const lableStyle = { color: '' };

        if (log.result.value === 'OK') {
          lableStyle.color = 'green';
        } else if (log.result.value === 'FAILED') {
          lableStyle.color = 'yellow';
        } else if (log.result.value === 'ERROR') {
          lableStyle.color = 'red';
        }

        const result = (
          <li key={index}>
            <span>
              {log.createTime}{' '}
              <span style={lableStyle}>{`${log.result.value} ${log.step} ${log.detail}`}</span>
            </span>
          </li>
        );
        index += 1;
        return result;
      };

      return (siberiaLogs || []).map(log => parseLog(log));
    };

    const renderAnsibleLogs = () => {
      let index = 0;
      const parseLog = log => {
        const lableStyle = { color: '' };
        const time = log.substring(0, 19);
        const content = log.substring(20);

        if (content.startsWith('ok: [')) {
          lableStyle.color = 'green';
        } else if (content.startsWith('changed: [')) {
          lableStyle.color = 'orange';
        } else if (content.startsWith('failed: [')) {
          lableStyle.color = 'yellow';
        } else if (content.startsWith('fatal: [')) {
          lableStyle.color = 'red';
        } else if (content.startsWith('included: ') || content.startsWith('skipping: [')) {
          lableStyle.color = '#00a7d0';
        } else if (content.startsWith('PLAY ') || content.startsWith('TASK [')) {
          lableStyle.color = '';
        }

        const result = (
          <li key={index}>
            <span>
              {time} <span style={lableStyle}>{content}</span>
            </span>
          </li>
        );
        index += 1;
        return result;
      };

      return (ansibleLogs || []).map(log => parseLog(log));
    };

    return (
      <PageHeaderLayout>
        <Card title="Siberia Log" className={styles.card} bordered={false}>
          <div className={styles.logHighlight}>
            <pre>{renderSiberiaLogs()}</pre>
          </div>
        </Card>

        <Card title="Ansible Log" className={styles.card} bordered={false}>
          <div className={styles.logHighlight}>
            <pre>
              <ul>{renderAnsibleLogs()}</ul>
            </pre>
          </div>
        </Card>
        <SockJsClient
          url="http://localhost:8080/wsendpoint"
          topics={[`/logs/siberia/${this.props.match.params.taskId}`]}
          onMessage={msg => this.pushSiberiaLog(msg)}
        />
        <SockJsClient
          url="http://localhost:8080/wsendpoint"
          topics={[`/logs/ansible/${this.props.match.params.taskId}`]}
          onMessage={msg => this.pushAnsibleLog(msg)}
        />
      </PageHeaderLayout>
    );
  }
}
