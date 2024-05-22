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

describe('Absence e2e test', () => {
  const absencePageUrl = '/absence';
  const absencePageUrlPattern = new RegExp('/absence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const absenceSample = {};

  let absence;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/absences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/absences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/absences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (absence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/absences/${absence.id}`,
      }).then(() => {
        absence = undefined;
      });
    }
  });

  it('Absences menu should load Absences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('absence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Absence').should('exist');
    cy.url().should('match', absencePageUrlPattern);
  });

  describe('Absence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(absencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Absence page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/absence/new$'));
        cy.getEntityCreateUpdateHeading('Absence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/absences',
          body: absenceSample,
        }).then(({ body }) => {
          absence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/absences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/absences?page=0&size=20>; rel="last",<http://localhost/api/absences?page=0&size=20>; rel="first"',
              },
              body: [absence],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(absencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Absence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('absence');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });

      it('edit button click should load edit Absence page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Absence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });

      it('edit button click should load edit Absence page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Absence');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });

      it('last delete button click should delete instance of Absence', () => {
        cy.intercept('GET', '/api/absences/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('absence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);

        absence = undefined;
      });
    });
  });

  describe('new Absence page', () => {
    beforeEach(() => {
      cy.visit(`${absencePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Absence');
    });

    it('should create an instance of Absence', () => {
      cy.get(`[data-cy="startDate"]`).type('2024-05-22T15:46');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-05-22T15:46');

      cy.get(`[data-cy="endDate"]`).type('2024-05-22T02:55');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-05-22T02:55');

      cy.get(`[data-cy="numberDayAbsence"]`).type('20653');
      cy.get(`[data-cy="numberDayAbsence"]`).should('have.value', '20653');

      cy.get(`[data-cy="typeAbsence"]`).select('CHOMAGE_PARTIEL');

      cy.get(`[data-cy="confirmationAbsence"]`).select('ENCOURS');

      cy.get(`[data-cy="congeRestant"]`).type('12162');
      cy.get(`[data-cy="congeRestant"]`).should('have.value', '12162');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        absence = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', absencePageUrlPattern);
    });
  });
});
