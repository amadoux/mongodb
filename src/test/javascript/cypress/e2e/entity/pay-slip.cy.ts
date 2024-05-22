import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('PaySlip e2e test', () => {
  const paySlipPageUrl = '/pay-slip';
  const paySlipPageUrlPattern = new RegExp('/pay-slip(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paySlipSample = {};

  let paySlip;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pay-slips+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pay-slips').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pay-slips/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paySlip) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pay-slips/${paySlip.id}`,
      }).then(() => {
        paySlip = undefined;
      });
    }
  });

  it('PaySlips menu should load PaySlips page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pay-slip');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaySlip').should('exist');
    cy.url().should('match', paySlipPageUrlPattern);
  });

  describe('PaySlip page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paySlipPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaySlip page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pay-slip/new$'));
        cy.getEntityCreateUpdateHeading('PaySlip');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paySlipPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pay-slips',
          body: paySlipSample,
        }).then(({ body }) => {
          paySlip = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pay-slips+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pay-slips?page=0&size=20>; rel="last",<http://localhost/api/pay-slips?page=0&size=20>; rel="first"',
              },
              body: [paySlip],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paySlipPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaySlip page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paySlip');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paySlipPageUrlPattern);
      });

      it('edit button click should load edit PaySlip page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaySlip');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paySlipPageUrlPattern);
      });

      it('edit button click should load edit PaySlip page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaySlip');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paySlipPageUrlPattern);
      });

      it('last delete button click should delete instance of PaySlip', () => {
        cy.intercept('GET', '/api/pay-slips/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('paySlip').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paySlipPageUrlPattern);

        paySlip = undefined;
      });
    });
  });

  describe('new PaySlip page', () => {
    beforeEach(() => {
      cy.visit(`${paySlipPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaySlip');
    });

    it('should create an instance of PaySlip', () => {
      cy.get(`[data-cy="netSalaryPay"]`).type('16066');
      cy.get(`[data-cy="netSalaryPay"]`).should('have.value', '16066');

      cy.get(`[data-cy="paySlipDate"]`).type('2024-05-21T23:44');
      cy.get(`[data-cy="paySlipDate"]`).blur();
      cy.get(`[data-cy="paySlipDate"]`).should('have.value', '2024-05-21T23:44');

      cy.setFieldImageAsBytesOfEntity('uploadPaySlip', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paySlip = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paySlipPageUrlPattern);
    });
  });
});
