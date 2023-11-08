import React from 'react';
import PropTypes from 'prop-types';
import AbstractEditForm from "../common/AbstractEditForm/AbstractEditForm";
import {FieldInputText} from "../common/components/fieldInputText";

export default function DocumentDetails(props) {
    const newItem = {
        description: '',
        organization: '',
        type: '',
        patient: ''
    };

    const fields = () => (
        <div >
            <div>
                <FieldInputText name='type' label='Вид документа' required/>
            </div>
            <div>
                <FieldInputText style={{display: "block"}} name='organization' label='Медицинская организация' required/>
            </div>
            <div>
                <FieldInputText style={{display: "block"}} name='description' label='Описание документа' required/>
            </div>
            <div>
                <FieldInputText name='patient' label='Пациент' required/>
            </div>
        </div>
    )

    return (
        <AbstractEditForm
            module='documents'
            stateName='documentState'
            newItem={newItem}
            renderFields={fields}
            onCloseRoute={`/`}
        />
    );
}

DocumentDetails.propTypes = {
    match: PropTypes.object.isRequired,
};
