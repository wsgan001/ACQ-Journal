set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

x=[1, 2, 3, 4, 5];
y1 = [0.076526039	0.114718082	0.12603187	0.131604134	0.134709966];
y2 = [0.313252851	0.478296782	0.538520429	0.568779084	0.58713628];
y3 = [0.303758741	0.4199578	0.466376904	0.492000678	0.50850937];
y4 = [0.086023841	0.145185356	0.174150007	0.190502902	0.208888088];

p1= plot(x, y1, '-k^');
hold on;
p2 = plot(x, y2, '-kv');
hold on;
p3 = plot(x, y3, '-ks');
hold on;
p4 = plot(x, y4, '-kd');

xlabel('the nubmer of shared keywords');
ylabel('CPJ');

axis([0.5 5.5 0.0 1.0]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'1','2','3','4','5'});
%leg=legend('Flickr','DBLP','Tencent','DBpedia', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'Flickr','DBLP');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4],'Tencent','DBpedia');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
%applyhatch(gcf,'/\x') ;
