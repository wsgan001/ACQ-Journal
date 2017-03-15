set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [2038.823094	1726.656061	1534.624852	1404.580452	1324.170293];
y2 = [2176.095212	1761.81795	1569.568507	1451.495308	1386.586258];
y3 = [179.7846424	159.9772691	155.0203639	141.8510629	134.4115903];
y4 = [141.8416215	130.9211995	128.5573833	122.2162846	120.8604103];
y5 = [49.78566734	46.79253901	44.60634139	44.7600489	42.50539361];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-ks');
hold on;
p5 = semilogy(x, y5, '-kd');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 10000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'basic-g','basic-w');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4, p5],'Inc-S','Inc-T','Dec');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


