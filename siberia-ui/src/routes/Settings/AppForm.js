import React, { PureComponent, Fragment } from 'react';
import { Table, Button, message, Popconfirm, Divider, Input } from 'antd';
import styles from './style.less';

export default class AppForm extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      loading: false,
      data: props.envList,
    };
  }

  componentWillReceiveProps(nextProps) {
    if ('envList' in nextProps) {
      this.setState({
        data: nextProps.envList,
      });
    }
  }
  getRowByKey(key, newData) {
    return (newData || this.state.data.content).filter(item => item.id === key)[0];
  }
  index = 0;
  cacheOriginData = {};
  toggleEditable = (e, key) => {
    e.preventDefault();
    const newData = this.state.data.content.map(item => ({ ...item }));
    const target = this.getRowByKey(key, newData);
    if (target) {
      // 进入编辑状态时保存原始数据
      if (!target.editable) {
        this.cacheOriginData[key] = { ...target };
      }
      target.editable = !target.editable;
      this.setState({ data: { content: newData } });
    }
  };
  removeApp = key => {
    const newData = this.state.data.content.filter(item => item.id !== key);
    this.props.remove(key);
    this.setState({ data: { content: newData } });
  };

  newApp = () => {
    const newData = this.state.data.content.map(item => ({ ...item }));
    newData.push({
      id: `NEW_TEMP_ID_${this.index}`,
      name: '',
      description: '',
      editable: true,
      isNew: true,
    });
    this.index += 1;
    this.setState({ data: { content: newData } });
  };
  handleKeyPress(e, key) {
    if (e.key === 'Enter') {
      this.saveRow(e, key);
    }
  }
  handleFieldChange(e, fieldName, key) {
    const newData = this.state.data.content.map(item => ({ ...item }));
    const target = this.getRowByKey(key, newData);
    if (target) {
      target[fieldName] = e.target.value;
      this.setState({ data: { content: newData } });
    }
  }
  saveRow(e, key) {
    e.persist();
    this.setState({
      loading: true,
    });
    if (this.clickedCancel) {
      this.clickedCancel = false;
      return;
    }
    const target = this.getRowByKey(key) || {};
    if (!target.module) {
      message.error('请填写完整环境信息');
      e.target.focus();
      this.setState({
        loading: false,
      });
      return;
    }
    delete target.isNew;
    this.toggleEditable(e, key);
    this.props.create(target);
    this.setState({
      loading: false,
    });
  }
  cancel(e, key) {
    this.clickedCancel = true;
    e.preventDefault();
    const newData = this.state.data.map(item => ({ ...item }));
    const target = this.getRowByKey(key, newData);
    if (this.cacheOriginData[key]) {
      Object.assign(target, this.cacheOriginData[key]);
      target.editable = false;
      delete this.cacheOriginData[key];
    }
    this.setState({ data: newData });
    this.clickedCancel = false;
  }
  render() {
    const columns = [
      {
        title: 'Project',
        dataIndex: 'project',
        key: 'project',
        width: '20%',
        render: (text, record) => {
          if (record.editable) {
            return (
              <Input
                value={text}
                autoFocus
                onChange={e => this.handleFieldChange(e, 'project', record.id)}
                onKeyPress={e => this.handleKeyPress(e, record.id)}
                placeholder="project name"
              />
            );
          }
          return text;
        },
      },
      {
        title: 'Module',
        dataIndex: 'module',
        key: 'module',
        width: '20%',
        render: (text, record) => {
          if (record.editable) {
            return (
              <Input
                value={text}
                autoFocus
                onChange={e => this.handleFieldChange(e, 'module', record.id)}
                onKeyPress={e => this.handleKeyPress(e, record.id)}
                placeholder="module name"
              />
            );
          }
          return text;
        },
      },
      {
        title: '类型',
        dataIndex: 'appType.desc',
        key: 'appType',
        width: '20%',
      },
      {
        title: '操作',
        key: 'action',
        render: (text, record) => {
          if (!!record.editable && this.state.loading) {
            return null;
          }
          if (record.editable) {
            if (record.isNew) {
              return (
                <span>
                  <a onClick={e => this.saveRow(e, record.id)}>添加</a>
                  <Divider type="vertical" />
                  <Popconfirm title="是否要删除此行？" onConfirm={() => this.remove(record.id)}>
                    <a>删除</a>
                  </Popconfirm>
                </span>
              );
            }
            return (
              <span>
                <a onClick={e => this.saveRow(e, record.id)}>保存</a>
                <Divider type="vertical" />
                <a onClick={e => this.cancel(e, record.id)}>取消</a>
              </span>
            );
          }
          return (
            <span>
              <a onClick={e => this.toggleEditable(e, record.id)}>编辑</a>
              <Divider type="vertical" />
              <Popconfirm title="是否要删除此行？" onConfirm={() => this.removeApp(record.id)}>
                <a>删除</a>
              </Popconfirm>
            </span>
          );
        },
      },
    ];

    return (
      <Fragment>
        <Table
          loading={this.state.loading}
          columns={columns}
          dataSource={this.state.data.content}
          pagination={false}
          rowClassName={record => {
            return record.editable ? styles.editable : '';
          }}
          rowKey="id"
        />
        <Button
          style={{ width: '100%', marginTop: 16, marginBottom: 8 }}
          type="dashed"
          onClick={this.newApp}
          icon="plus"
        >
          新增应用配置
        </Button>
      </Fragment>
    );
  }
}
