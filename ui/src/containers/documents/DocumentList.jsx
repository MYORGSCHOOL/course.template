import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';
import EnhancedTable from "../common/components/table/table";
import * as api from '../../api/cruds-api';
import Loader from "../Loader";


const headCells = [
    { property: 'id', disablePadding: true, label: 'Номер' },
    { property: 'type', disablePadding: true, label: 'Вид документа' },
    { property: 'organization', numeric: true, disablePadding: false, label: 'Медицинская организация' },
    { property: 'date', numeric: true, disablePadding: false, label: 'Дата' },
    { property: 'description', numeric: true, disablePadding: false, label: 'Описание документа' },
    { property: 'patient', numeric: true, disablePadding: false, label: 'Пациент' },
    { property: 'status', numeric: true, disablePadding: false, label: 'Статус' }
];

// eslint-disable-next-line no-unused-vars
export default function DocumentList() {
    const history = useHistory();

    const dispatch = useDispatch();

    const {list, isLoading} = useSelector(state => state.documentState);

    const getList = () => api.getList(dispatch, 'documents');

    useEffect(() => getList(), []);

    if (isLoading) {
        return (
            <Loader isLoading = {isLoading}/>
        );
    }

    const handleDeleteItem = (selected) => api.deleteItems(dispatch, 'documents', selected).then(() => {
       getList()
    }).catch(error => {
        alert(error)
    });

    const handleSendItem = (selected) => api.send(dispatch, 'documents', selected[0]).then(() => {
        getList()
    }).catch(error => {
        console.log('error',error)
        getList()
    });

    return (
        <div>
            <EnhancedTable
                dataMap={headCells}
                toolbar={{
                    toolbarTitle: 'Все документы',
                    delete: (selected) => handleDeleteItem(selected),
                    send: (selected) => handleSendItem(selected),
                    add: () => history.push(`/documents/new`)
                }}
                rows={list}
            />
        </div>
    );
}
